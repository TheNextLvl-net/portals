package net.thenextlvl.portals;

import core.i18n.file.ComponentBundle;
import net.kyori.adventure.key.Key;
import net.thenextlvl.portals.config.PortalConfig;
import net.thenextlvl.portals.config.SimplePortalConfig;
import net.thenextlvl.portals.listener.PortalListener;
import net.thenextlvl.portals.portal.PaperPortalProvider;
import net.thenextlvl.portals.selection.NativeSelectionProvider;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.selection.WorldEditSelectionProvider;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
public final class PortalsPlugin extends JavaPlugin {
    private final PaperPortalProvider portalProvider = new PaperPortalProvider(this);
    private final PortalConfig portalConfig = SimplePortalConfig.INSTANCE;

    private final Metrics metrics = new Metrics(this, 27514);

    private final ComponentBundle bundle = ComponentBundle.builder(
                    Key.key("portals", "translations"),
                    getDataPath().resolve("translations")
            ).resource("messages.properties", Locale.US)
            .resource("messages_german.properties", Locale.GERMANY)
            .placeholder("prefix", "prefix")
            .build();

    public PortalsPlugin() {
        getServer().getServicesManager().register(PortalProvider.class, portalProvider, this, ServicePriority.Highest);
        getServer().getServicesManager().register(SelectionProvider.class, new NativeSelectionProvider(), this, ServicePriority.Lowest);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        if (getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
            getServer().getServicesManager().register(SelectionProvider.class, new WorldEditSelectionProvider(this), this, ServicePriority.Normal);
        }
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    public PaperPortalProvider portalProvider() {
        return portalProvider;
    }

    public ComponentBundle bundle() {
        return bundle;
    }

    public PortalConfig config() {
        return portalConfig;
    }
}
