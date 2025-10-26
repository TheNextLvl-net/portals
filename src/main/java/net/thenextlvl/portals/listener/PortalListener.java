package net.thenextlvl.portals.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.event.EntityPortalEnterEvent;
import net.thenextlvl.portals.event.EntityPortalExitEvent;
import net.thenextlvl.portals.event.PreEntityPortalEnterEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class PortalListener implements Listener {
    private final PortalsPlugin plugin;
    private final Map<Portal, Map<UUID, Instant>> lastEntry = new HashMap<>();
    private final Map<UUID, Portal> lastPortal = new HashMap<>();

    public PortalListener(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean hasCooldown(Portal portal, Entity entity) {
        var entries = this.lastEntry.get(portal);
        if (entries == null) return false;
        var lastEntry = entries.get(entity.getUniqueId());
        return lastEntry != null && Instant.now().isBefore(lastEntry.plus(portal.getCooldown()));
    }

    private void setLastEntry(Portal portal, Entity entity) {
        lastEntry.computeIfAbsent(portal, ignored -> new HashMap<>())
                .put(entity.getUniqueId(), Instant.now());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityMove(EntityMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (processMovement(event.getEntity(), event.getTo())) return;
        pushAway(event.getEntity(), event.getTo());
        event.setCancelled(true);
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityPortalEnter(org.bukkit.event.entity.EntityPortalEnterEvent event) {
        plugin.portalProvider().getPortals(event.getLocation().getWorld())
                .filter(portal -> portal.getBoundingBox().contains(event.getLocation()))
                .findAny().ifPresent(portal -> event.setCancelled(true));
    }

    private boolean processMovement(Entity entity, Location to) {
        var boundingBox = translate(entity.getBoundingBox(), to);
        return plugin.portalProvider().getPortals(entity.getWorld())
                .filter(portal -> portal.getBoundingBox().overlaps(boundingBox))
                .findAny().map(portal -> {
                    if (portal.equals(lastPortal.get(entity.getUniqueId()))) return true;

                    if (!new PreEntityPortalEnterEvent(portal, entity).callEvent()) return false;

                    if (entity.getType().equals(EntityType.PLAYER) && !portal.getEntryPermission()
                            .map(entity::hasPermission).orElse(true)) return false;
                    if (portal.getCooldown().isPositive() && hasCooldown(portal, entity)) return false;
                    if (portal.getEntryCost() > 0 && !withdrawEntryCost(portal, entity)) return false;

                    if (!new EntityPortalEnterEvent(portal, entity).callEvent()) return false;

                    portal.getEntryAction().ifPresent(action -> action.onEntry(portal, entity));
                    if (portal.getCooldown().isPositive()) setLastEntry(portal, entity);
                    lastPortal.put(entity.getUniqueId(), portal);
                    return true;
                }).orElseGet(() -> {
                    var removed = lastPortal.remove(entity.getUniqueId());
                    if (removed != null) new EntityPortalExitEvent(removed, entity).callEvent();
                    return true;
                });
    }

    private org.bukkit.util.BoundingBox translate(org.bukkit.util.BoundingBox boundingBox, Location location) {
        var widthX = boundingBox.getWidthX() / 2;
        var widthZ = boundingBox.getWidthZ() / 2;

        return new org.bukkit.util.BoundingBox(
                location.getX() - widthX, location.getY(), location.getZ() - widthZ,
                location.getX() + widthX, location.getY() + boundingBox.getHeight(), location.getZ() + widthZ
        );
    }

    private void pushAway(Entity entity, Location to) {
        if (!plugin.config().pushBackOnEntryDenied()) return;
        entity.getScheduler().run(plugin, task -> {
            var direction = entity.getLocation().toVector().subtract(to.toVector()).normalize();
            entity.setVelocity(direction.multiply(plugin.config().pushBackSpeed()));
        }, null);
    }

    private boolean withdrawEntryCost(Portal portal, Entity entity) {
        if (!plugin.config().entryCosts()) return true;
        if (!(entity instanceof Player player)) return true;
        // return entity.withdrawMoney(portal.getEntryCost()); // vault integration
        return true;
    }
}
