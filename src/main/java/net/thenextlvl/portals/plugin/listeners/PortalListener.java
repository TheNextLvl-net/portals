package net.thenextlvl.portals.plugin.listeners;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jspecify.annotations.NullMarked;

import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.title.Title;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.event.EntityPortalEnterEvent;
import net.thenextlvl.portals.event.EntityPortalExitEvent;
import net.thenextlvl.portals.event.PreEntityPortalEnterEvent;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.utils.Debugger;

@NullMarked
public final class PortalListener implements Listener {
    private static final Map<Portal, Map<UUID, Instant>> lastEntry = new HashMap<>();
    private static final Map<UUID, Portal> lastPortal = new HashMap<>();
    private static final Map<UUID, Warmup> warmups = new HashMap<>();

    private final PortalsPlugin plugin;

    public PortalListener(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (processMovement(event.getPlayer(), event.getTo())) return;
        pushAway(event.getPlayer(), event.getTo());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;
        if (processMovement(event.getPlayer(), event.getPlayer().getLocation())) return;
        event.setCancelled(true);
    }

    private boolean processMovement(Entity entity, Location to) {
        if (isInWarmup(entity)) {
            resetWarmupIfPresent(entity);
            return true;
        }
        var boundingBox = translate(entity.getBoundingBox(), to);
        return plugin.portalProvider().getPortals(entity.getWorld())
                .filter(portal -> portal.getBoundingBox().overlaps(boundingBox))
                .findAny().map(portal -> {
                    if (portal.equals(lastPortal.get(entity.getUniqueId()))) return true;
                    var debugger = plugin.debugger;
                    debugger.newTransaction();
                    debugger.log("'%s' entered the portal '%s'", entity.getName(), portal.getName());

                    if (!new PreEntityPortalEnterEvent(portal, entity).callEvent()) {
                        debugger.log("PreEntityPortalEnterEvent was cancelled for '%s' in '%s'", entity.getName(), portal.getName());
                        return false;
                    }

                    if (!portal.getEntryPermission().map(entity::hasPermission).orElse(true)) {
                        debugger.log("EntryPermission was not met for '%s' in '%s' (%s)", entity.getName(), portal.getName(), portal.getEntryPermission().orElse(null));
                        return false;
                    }
                    if (portal.getCooldown().isPositive() && hasCooldown(portal, entity)) {
                        debugger.log("Cooldown was not met for '%s' in '%s' (%s left)", entity.getName(), portal.getName(), debugger.durationToString(getRemainingCooldown(portal, entity)));
                        return false;
                    }
                    if (portal.getEntryCost() > 0 && !withdrawEntryCost(portal, entity)) {
                        debugger.log("EntryCost was not met for '%s' in '%s' (%s)", entity.getName(), portal.getName(), portal.getEntryCost());
                        return false;
                    }

                    if (!new EntityPortalEnterEvent(portal, entity).callEvent()) {
                        debugger.log("EntityPortalEnterEvent was cancelled for '%s' in '%s'", entity.getName(), portal.getName());
                        return false;
                    }

                    if (portal.getCooldown().isPositive()) setLastEntry(portal, entity);
                    setLastPortal(entity, portal);

                    if (portal.getWarmup().isPositive()) {
                        startWarmup(entity, portal, debugger);
                        return true;
                    }

                    return portal.getEntryAction().map(action -> {
                        if (action.onEntry(entity, portal)) {
                            debugger.log("EntryAction was successful for '%s' in '%s'", entity.getName(), portal.getName());
                            return true;
                        }
                        debugger.log("EntryAction was cancelled for '%s' in '%s'", entity.getName(), portal.getName());
                        return false;
                    }).orElseGet(() -> {
                        debugger.log("No EntryAction for '%s' in '%s'", entity.getName(), portal.getName());
                        return true;
                    });
                }).orElseGet(() -> {
                    var removed = lastPortal.remove(entity.getUniqueId());
                    warmups.remove(entity.getUniqueId());
                    if (removed != null) new EntityPortalExitEvent(removed, entity).callEvent();
                    return true;
                });
    }

    private void startWarmup(Entity entity, Portal portal, Debugger debugger) {
        var warmup = portal.getWarmup();
        var uuid = entity.getUniqueId();

        warmups.put(uuid, new Warmup(portal));
        resetWarmupIfPresent(entity);

        if (entity instanceof Player player) {
            plugin.bundle().sendMessage(player, "portal.warmup.start",
                    Formatter.number("warmup", warmup.toMillis() / 1000d));
        }

        scheduleWarmupCheck(entity, portal, debugger, warmup);
    }

    private void scheduleWarmupCheck(Entity entity, Portal portal, Debugger debugger, Duration delay) {
        var uuid = entity.getUniqueId();
        var ticks = Tick.tick().fromDuration(delay);
        if (ticks <= 0) ticks = 1;

        entity.getScheduler().runDelayed(plugin, task -> {
            var current = warmups.get(uuid);
            if (current == null || !current.portal().equals(portal)) return;

            if (!portal.getBoundingBox().contains(entity.getLocation())) {
                warmups.remove(uuid);
                return;
            }

            var elapsed = Duration.between(current.startedAt(), Instant.now());
            var remaining = portal.getWarmup().minus(elapsed);
            if (remaining.isPositive()) {
                scheduleWarmupCheck(entity, portal, debugger, remaining);
                return;
            }

            var success = portal.getEntryAction().map(action -> {
                if (action.onEntry(entity, portal)) {
                    debugger.log("EntryAction was successful for '%s' in '%s' (after warmup)", entity.getName(), portal.getName());
                    return true;
                }
                debugger.log("EntryAction was cancelled for '%s' in '%s' (after warmup)", entity.getName(), portal.getName());
                return false;
            }).orElseGet(() -> {
                debugger.log("No EntryAction for '%s' in '%s' (after warmup)", entity.getName(), portal.getName());
                return true;
            });

            warmups.remove(uuid);
            if (!success) return;
        }, null, ticks);
    }

    private void resetWarmupIfPresent(Entity entity) {
        var warmup = warmups.get(entity.getUniqueId());
        if (warmup == null) return;
        var startedAt = warmup.startedAt();
        warmup.reset();
        if (startedAt.equals(Instant.EPOCH)) return;
        if (!(entity instanceof Player player)) return;

        var total = warmup.portal().getWarmup();
        if (!total.isPositive()) return;

        var elapsed = Duration.between(startedAt, Instant.now());
        if (elapsed.toMillis() < total.toMillis() * 0.4) return;

        plugin.bundle().sendMessage(player, "portal.warmup.reset",
                Formatter.number("warmup", total.toMillis() / 1000d));

        var title = plugin.bundle().component("portal.warmup.reset.title", player,
                Formatter.number("warmup", total.toMillis() / 1000d));
        var subtitle = plugin.bundle().component("portal.warmup.reset.subtitle", player,
                Formatter.number("warmup", total.toMillis() / 1000d));
        player.showTitle(Title.title(title, subtitle));
    }

    private boolean isInWarmup(Entity entity) {
        return warmups.containsKey(entity.getUniqueId());
    }

    private void pushAway(Entity entity, Location to) {
        var speed = plugin.config().pushbackSpeed();
        if (speed > 0) entity.getScheduler().run(plugin, task -> {
            var direction = entity.getLocation().toVector().subtract(to.toVector()).normalize();
            entity.setVelocity(direction.multiply(speed));
        }, null);
    }

    private boolean withdrawEntryCost(Portal portal, Entity entity) {
        if (!plugin.config().entryCosts()) return true;
        if (!(entity instanceof Player player)) return true;
        return plugin.economyProvider().withdraw(player, portal.getEntryCost());
    }

    public static void setLastPortal(Entity entity, Portal portal) {
        lastPortal.put(entity.getUniqueId(), portal);
    }

    private static org.bukkit.util.BoundingBox translate(org.bukkit.util.BoundingBox boundingBox, Location location) {
        var widthX = boundingBox.getWidthX() / 2;
        var widthZ = boundingBox.getWidthZ() / 2;

        return new org.bukkit.util.BoundingBox(
                location.getX() - widthX, location.getY(), location.getZ() - widthZ,
                location.getX() + widthX, location.getY() + boundingBox.getHeight(), location.getZ() + widthZ
        );
    }

    private static boolean hasCooldown(Portal portal, Entity entity) {
        return getRemainingCooldown(portal, entity).isPositive();
    }

    private static Duration getRemainingCooldown(Portal portal, Entity entity) {
        var entries = lastEntry.get(portal);
        if (entries == null) return Duration.ZERO;
        var lastEntry = entries.get(entity.getUniqueId());
        if (lastEntry == null) return Duration.ZERO;
        return Duration.between(Instant.now(), lastEntry.plus(portal.getCooldown()));
    }

    private static void setLastEntry(Portal portal, Entity entity) {
        lastEntry.computeIfAbsent(portal, ignored -> new HashMap<>())
                .put(entity.getUniqueId(), Instant.now());
    }

    private static final class Warmup {
        private final Portal portal;
        private Instant startedAt = Instant.EPOCH;

        private Warmup(Portal portal) {
            this.portal = portal;
        }

        private Portal portal() {
            return portal;
        }

        private Instant startedAt() {
            return startedAt;
        }

        private void reset() {
            this.startedAt = Instant.now();
        }
    }
}
