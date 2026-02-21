package net.thenextlvl.portals.plugin.listeners;

import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EntityListener implements Listener {
    private final PortalsPlugin plugin;

    public EntityListener(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByBlock(final EntityDamageByBlockEvent event) {
        if (event.getDamager() == null) return;
        final var location = event.getDamager().getLocation();
        event.setCancelled(plugin.portalProvider().getPortals(location.getWorld())
                .anyMatch(portal -> portal.getBoundingBox().contains(location)));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByBlock(final EntityCombustByBlockEvent event) {
        if (event.getCombuster() == null) return;
        final var location = event.getCombuster().getLocation();
        event.setCancelled(plugin.portalProvider().getPortals(location.getWorld())
                .anyMatch(portal -> portal.getBoundingBox().contains(location)));
    }
}
