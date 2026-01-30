package net.thenextlvl.portals.plugin.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.event.EntityPortalEnterEvent;
import net.thenextlvl.portals.event.EntityPortalExitEvent;
import net.thenextlvl.portals.event.EntityPortalWarmupCancelEvent;
import net.thenextlvl.portals.event.EntityPortalWarmupEvent;
import net.thenextlvl.portals.event.PreEntityPortalEnterEvent;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class PortalListener implements Listener {
    private static final Map<Portal, Map<UUID, Instant>> lastEntry = new HashMap<>();
    private static final Map<UUID, Portal> lastPortal = new HashMap<>();
    private static final Map<UUID, Warmup> warmups = new HashMap<>();

    private final PortalsPlugin plugin;

    public PortalListener(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityMove(final EntityMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (plugin.config().ignoreEntityMovement()) return;
        if (processMovement(event.getEntity(), event.getTo())) return;
        pushAway(event.getEntity(), event.getTo());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (processMovement(event.getPlayer(), event.getTo())) return;
        pushAway(event.getPlayer(), event.getTo());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerToggleSneak(final PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;
        if (processMovement(event.getPlayer(), event.getPlayer().getLocation())) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityPortalEnter(final org.bukkit.event.entity.EntityPortalEnterEvent event) {
        plugin.portalProvider().getPortals(event.getLocation().getWorld())
                .filter(portal -> portal.getBoundingBox().contains(event.getLocation()))
                .findAny().ifPresent(portal -> event.setCancelled(true));
    }

    private boolean processMovement(final Entity entity, final Location to) {
        final var boundingBox = translate(entity.getBoundingBox(), to);
        return plugin.portalProvider().getPortals(entity.getWorld())
                .filter(portal -> portal.getBoundingBox().overlaps(boundingBox))
                .findAny().map(portal -> {
                    if (portal.equals(lastPortal.get(entity.getUniqueId()))) return true;
                    final var debugger = plugin.debugger;
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

                    resetWarmupIfPresent(entity);
                    if (portal.getWarmup().isPositive()) {
                        startWarmup(entity, portal);
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
                    resetWarmupIfPresent(entity);
                    final var removed = lastPortal.remove(entity.getUniqueId());
                    if (removed != null) new EntityPortalExitEvent(removed, entity).callEvent();
                    return true;
                });
    }

    private void startWarmup(final Entity entity, final Portal portal) {
        final var scheduledTask = scheduleWarmupCheck(entity, portal, portal.getWarmup());
        if (scheduledTask == null) return;

        final var finished = Instant.now().plus(portal.getWarmup());
        warmups.put(entity.getUniqueId(), new Warmup(portal, finished, scheduledTask));

        final var seconds = portal.getWarmup().toMillis() / 1000d;
        plugin.bundle().sendMessage(entity, "portal.warmup.start",
                Formatter.number("warmup", seconds),
                Formatter.booleanChoice("plural", seconds != 1));

        new EntityPortalWarmupEvent(portal, entity).callEvent();
    }

    private @Nullable ScheduledTask scheduleWarmupCheck(final Entity entity, final Portal portal, final Duration delay) {
        final var debugger = plugin.debugger;

        debugger.log("Starting warmup for '%s' in '%s' (%s left)", entity.getName(), portal.getName(), debugger.durationToString(delay));

        return entity.getScheduler().runDelayed(plugin, task -> {
            warmups.remove(entity.getUniqueId());

            portal.getEntryAction().ifPresentOrElse(action -> {
                if (action.onEntry(entity, portal)) {
                    debugger.log("EntryAction was successful for '%s' in '%s' (after warmup)", entity.getName(), portal.getName());
                } else {
                    debugger.log("EntryAction was cancelled for '%s' in '%s' (after warmup)", entity.getName(), portal.getName());
                }
            }, () -> {
                debugger.log("No EntryAction for '%s' in '%s' (after warmup)", entity.getName(), portal.getName());
            });
        }, () -> {
            debugger.log("Cancelled warmup for '%s' in '%s' (retired)", entity.getName(), portal.getName());
            warmups.remove(entity.getUniqueId());
        }, Math.max(1, Tick.tick().fromDuration(delay)));
    }

    private void resetWarmupIfPresent(final Entity entity) {
        final var warmup = warmups.remove(entity.getUniqueId());
        if (warmup == null) return;

        warmup.task().cancel();

        final var remaining = Duration.between(Instant.now(), warmup.finished());
        new EntityPortalWarmupCancelEvent(warmup.portal(), entity, remaining).callEvent();

        final var debugger = plugin.debugger;
        debugger.log("Cancelled warmup for '%s' in '%s' (%s left)", entity.getName(), warmup.portal().getName(), debugger.durationToString(remaining));
    }

    private void pushAway(final Entity entity, final Location to) {
        final var speed = plugin.config().pushbackSpeed();
        if (speed > 0) entity.getScheduler().run(plugin, task -> {
            final var direction = entity.getLocation().toVector().subtract(to.toVector()).normalize();
            entity.setVelocity(direction.multiply(speed));
        }, null);
    }

    private boolean withdrawEntryCost(final Portal portal, final Entity entity) {
        if (!plugin.config().entryCosts()) return true;
        if (!(entity instanceof final Player player)) return true;
        return plugin.economyProvider().withdraw(player, portal.getEntryCost());
    }

    public static void setLastPortal(final Entity entity, final Portal portal) {
        lastPortal.put(entity.getUniqueId(), portal);
    }

    private static org.bukkit.util.BoundingBox translate(final org.bukkit.util.BoundingBox boundingBox, final Location location) {
        final var widthX = boundingBox.getWidthX() / 2;
        final var widthZ = boundingBox.getWidthZ() / 2;

        return new org.bukkit.util.BoundingBox(
                location.getX() - widthX, location.getY(), location.getZ() - widthZ,
                location.getX() + widthX, location.getY() + boundingBox.getHeight(), location.getZ() + widthZ
        );
    }

    private static boolean hasCooldown(final Portal portal, final Entity entity) {
        return getRemainingCooldown(portal, entity).isPositive();
    }

    private static Duration getRemainingCooldown(final Portal portal, final Entity entity) {
        final var entries = lastEntry.get(portal);
        if (entries == null) return Duration.ZERO;
        final var lastEntry = entries.get(entity.getUniqueId());
        if (lastEntry == null) return Duration.ZERO;
        return Duration.between(Instant.now(), lastEntry.plus(portal.getCooldown()));
    }

    private static void setLastEntry(final Portal portal, final Entity entity) {
        lastEntry.computeIfAbsent(portal, ignored -> new HashMap<>())
                .put(entity.getUniqueId(), Instant.now());
    }

    private record Warmup(Portal portal, Instant finished, ScheduledTask task) {
    }
}
