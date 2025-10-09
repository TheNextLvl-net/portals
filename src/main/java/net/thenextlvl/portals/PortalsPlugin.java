package net.thenextlvl.portals;

import net.thenextlvl.portals.listener.PortalListener;
import net.thenextlvl.portals.portal.PaperPortalProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalsPlugin extends JavaPlugin {
    private final PaperPortalProvider portalProvider = new PaperPortalProvider(this);

    public PortalsPlugin() {
        getServer().getServicesManager().register(PortalProvider.class, portalProvider, this, ServicePriority.Highest);
    }
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
    }

    public PaperPortalProvider portalProvider() {
        return portalProvider;
    }
}
