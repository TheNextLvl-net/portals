package net.thenextlvl.portals.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.event.EntityPortalEnterEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class PortalListener implements Listener {
    private final PortalsPlugin plugin;
    private final Map<Portal, Map<UUID, Instant>> lastEntry = new HashMap<>();

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
        if (processMovement(event.getEntity(), event.getTo())) return;
        pushBack(event.getEntity());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (processMovement(event.getPlayer(), event.getTo())) return;
        pushBack(event.getPlayer());
        event.setCancelled(true);
    }

    private boolean processMovement(Entity entity, Location location) {
        return plugin.portalProvider().getPortals(location.getWorld())
                .filter(portal -> portal.getBoundingBox().contains(location))
                .findAny().map(portal -> {
                    if (!new EntityPortalEnterEvent(portal, entity).callEvent()) return false;
                    if (!portal.getEntryPermission().map(entity::hasPermission).orElse(true)) return false;
                    if (portal.getCooldown().isPositive() && hasCooldown(portal, entity)) return false;
                    if (portal.getEntryCost() > 0 && !withdrawEntryCost(portal, entity)) return false;
                    portal.getEntryAction().ifPresent(action -> action.onEntry(portal, entity));
                    if (portal.getCooldown().isPositive()) setLastEntry(portal, entity);
                    return true;
                }).orElse(true);
    }

    private void pushBack(Entity entity) {
        entity.setVelocity(entity.getVelocity().multiply(-1));
    }

    private boolean withdrawEntryCost(Portal portal, Entity entity) {
        // return entity.withdrawMoney(portal.getEntryCost()); // vault integration
        return true;
    }
}
