package net.thenextlvl.portals.listener;

import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WorldListener implements Listener {
    private final PortalsPlugin plugin;

    public WorldListener(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        plugin.portalProvider().getPortals(event.getWorld()).forEach(Portal::persist);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        plugin.portalProvider().portals.removeIf(portal -> {
            if (portal.getWorld().equals(event.getWorld())) {
                portal.persist();
                return true;
            } else return false;
        });
    }

    // todo: load portals from disk when world is loaded
}
