package net.thenextlvl.portals;

import core.i18n.file.ComponentBundle;
import io.papermc.paper.math.Position;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.thenextlvl.binder.StaticBinder;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.adapter.BoundingBoxAdapter;
import net.thenextlvl.portals.adapter.EntryActionAdapter;
import net.thenextlvl.portals.adapter.KeyAdapter;
import net.thenextlvl.portals.adapter.PortalAdapter;
import net.thenextlvl.portals.adapter.PositionAdapter;
import net.thenextlvl.portals.command.PortalCommand;
import net.thenextlvl.portals.model.PortalConfig;
import net.thenextlvl.portals.model.SimplePortalConfig;
import net.thenextlvl.portals.economy.EconomyProvider;
import net.thenextlvl.portals.economy.EmptyEconomyProvider;
import net.thenextlvl.portals.economy.ServiceEconomyProvider;
import net.thenextlvl.portals.economy.VaultEconomyProvider;
import net.thenextlvl.portals.listener.PortalListener;
import net.thenextlvl.portals.listener.WorldListener;
import net.thenextlvl.portals.portal.PaperPortalProvider;
import net.thenextlvl.portals.selection.NativeSelectionProvider;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.selection.WorldEditSelectionProvider;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bstats.bukkit.Metrics;
import org.bukkit.World;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
public final class PortalsPlugin extends JavaPlugin {
    public static final String ISSUES = "https://github.com/TheNextLvl-net/portals/issues/new";

    private final PaperPortalProvider portalProvider = new PaperPortalProvider(this);
    private final PortalConfig portalConfig = SimplePortalConfig.INSTANCE;
    private EconomyProvider economyProvider = new EmptyEconomyProvider();

    private final Metrics metrics = new Metrics(this, 27514);

    private final ComponentBundle bundle = ComponentBundle.builder(
                    Key.key("portals", "translations"),
                    getDataPath().resolve("translations")
            ).resource("messages.properties", Locale.US)
            .resource("messages_german.properties", Locale.GERMANY)
            .placeholder("prefix", "prefix")
            .build();

    public PortalsPlugin() {
        StaticBinder.getInstance(PortalProvider.class.getClassLoader()).bind(PortalProvider.class, portalProvider);
        getServer().getServicesManager().register(SelectionProvider.class, new NativeSelectionProvider(), this, ServicePriority.Lowest);
        registerCommands();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);

        if (getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
            getServer().getServicesManager().register(SelectionProvider.class, new WorldEditSelectionProvider(), this, ServicePriority.Normal);
        }
        if (getServer().getPluginManager().isPluginEnabled("ServiceIO")) {
            this.economyProvider = new ServiceEconomyProvider(this);
        } else if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            this.economyProvider = new VaultEconomyProvider(this);
        }
    }

    @Override
    public void onDisable() {
        portalProvider.forEachPortal(Portal::persist);
        metrics.shutdown();
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            event.registrar().register(PortalCommand.create(this), "The main command to interact with portals");
        }));
    }

    @Contract(pure = true)
    public PaperPortalProvider portalProvider() {
        return portalProvider;
    }

    @Contract(pure = true)
    public EconomyProvider economyProvider() {
        return economyProvider;
    }

    @Contract(pure = true)
    public ComponentBundle bundle() {
        return bundle;
    }

    @Contract(pure = true)
    public PortalConfig config() {
        return portalConfig;
    }

    @Contract(value = "_ -> new", pure = true)
    public NBT nbt(World world) {
        return NBT.builder()
                .registerTypeHierarchyAdapter(BoundingBox.class, new BoundingBoxAdapter(world))
                .registerTypeHierarchyAdapter(EntryAction.class, new EntryActionAdapter(this))
                .registerTypeHierarchyAdapter(Position.class, new PositionAdapter())
                .registerTypeHierarchyAdapter(Key.class, new KeyAdapter())
                .registerTypeHierarchyAdapter(Portal.class, new PortalAdapter(this))
                .build();
    }
}
