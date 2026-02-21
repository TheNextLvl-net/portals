package net.thenextlvl.portals.plugin;

import com.google.common.io.ByteStreams;
import core.file.FileIO;
import core.file.formats.GsonFile;
import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.ErrorTracker;
import io.papermc.paper.math.Position;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import net.thenextlvl.binder.StaticBinder;
import net.thenextlvl.i18n.ComponentBundle;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.nbt.serialization.adapters.EnumAdapter;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalProvider;
import net.thenextlvl.portals.action.ActionTypeRegistry;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.bounds.BoundsFactory;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.plugin.action.SimpleActionTypeRegistry;
import net.thenextlvl.portals.plugin.action.SimpleActionTypes;
import net.thenextlvl.portals.plugin.adapters.BoundingBoxAdapter;
import net.thenextlvl.portals.plugin.adapters.EntryActionAdapter;
import net.thenextlvl.portals.plugin.adapters.FinePositionAdapter;
import net.thenextlvl.portals.plugin.adapters.KeyAdapter;
import net.thenextlvl.portals.plugin.adapters.NotificationTriggerAdapter;
import net.thenextlvl.portals.plugin.adapters.NotificationTypeAdapter;
import net.thenextlvl.portals.plugin.adapters.PortalAdapter;
import net.thenextlvl.portals.plugin.adapters.SoundAdapter;
import net.thenextlvl.portals.plugin.adapters.TitleTimesAdapter;
import net.thenextlvl.portals.plugin.adapters.UnparsedTitleAdapter;
import net.thenextlvl.portals.plugin.bounds.SimpleBoundsFactory;
import net.thenextlvl.portals.plugin.commands.PortalCommand;
import net.thenextlvl.portals.plugin.economy.EconomyProvider;
import net.thenextlvl.portals.plugin.economy.EmptyEconomyProvider;
import net.thenextlvl.portals.plugin.economy.ServiceEconomyProvider;
import net.thenextlvl.portals.plugin.economy.VaultEconomyProvider;
import net.thenextlvl.portals.plugin.listeners.EntityListener;
import net.thenextlvl.portals.plugin.listeners.PortalListener;
import net.thenextlvl.portals.plugin.listeners.WorldListener;
import net.thenextlvl.portals.plugin.model.SimplePortalConfig;
import net.thenextlvl.portals.plugin.portal.PaperPortal;
import net.thenextlvl.portals.plugin.portal.PaperPortalProvider;
import net.thenextlvl.portals.plugin.selections.WorldEditSelectionProvider;
import net.thenextlvl.portals.plugin.utils.Debugger;
import net.thenextlvl.portals.plugin.version.PluginVersionChecker;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.shape.BoundingBox;
import net.thenextlvl.portals.view.PortalConfig;
import net.thenextlvl.portals.view.UnparsedTitle;
import org.bstats.bukkit.Metrics;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
public final class PortalsPlugin extends JavaPlugin {
    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();
    public static final String ISSUES = "https://github.com/TheNextLvl-net/portals/issues/new?template=bug_report.yml";

    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);

    private final PaperPortalProvider portalProvider = new PaperPortalProvider(this);
    private EconomyProvider economyProvider = new EmptyEconomyProvider();

    private final Metrics metrics = new Metrics(this, 27514);
    private final dev.faststats.core.Metrics fastStats = BukkitMetrics.factory()
            .token("871d4095811865739273cb8ce0e65302")
            .errorTracker(ERROR_TRACKER)
            .create(this);

    private final FileIO<SimplePortalConfig> portalConfig = new GsonFile<>(
            getDataPath().resolve("config.json"),
            new SimplePortalConfig(false, true, false, true, 0.3),
            SimplePortalConfig.class
    ).validate().save();

    private final ComponentBundle bundle = ComponentBundle.builder(
                    Key.key("portals", "translations"),
                    getDataPath().resolve("translations")
            ).resource("messages.properties", Locale.US)
            .resource("messages_german.properties", Locale.GERMANY)
            .placeholder("prefix", "prefix")
            .build();

    public final Debugger debugger = new Debugger(this);

    public PortalsPlugin() {
        StaticBinder.getInstance(ActionTypes.class.getClassLoader()).bind(ActionTypes.class, SimpleActionTypes.INSTANCE);
        StaticBinder.getInstance(BoundsFactory.class.getClassLoader()).bind(BoundsFactory.class, SimpleBoundsFactory.INSTANCE);
        StaticBinder.getInstance(ActionTypeRegistry.class.getClassLoader()).bind(ActionTypeRegistry.class, SimpleActionTypeRegistry.INSTANCE);
        StaticBinder.getInstance(PortalConfig.class.getClassLoader()).bind(PortalConfig.class, portalConfig.getRoot());
        StaticBinder.getInstance(PortalProvider.class.getClassLoader()).bind(PortalProvider.class, portalProvider);

        registerCommands();
    }

    @Override
    public void onEnable() {
        fastStats.ready();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
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
        return portalConfig.getRoot();
    }

    @Contract(value = "_ -> new", pure = true)
    public NBT nbt(final World world) {
        return NBT.builder()
                .registerTypeHierarchyAdapter(BoundingBox.class, new BoundingBoxAdapter(world))
                .registerTypeHierarchyAdapter(EntryAction.class, new EntryActionAdapter(this))
                .registerTypeHierarchyAdapter(Position.class, new FinePositionAdapter())
                .registerTypeHierarchyAdapter(Key.class, new KeyAdapter())
                .registerTypeHierarchyAdapter(PaperPortal.class, new PortalAdapter(this))
                .registerTypeHierarchyAdapter(NotificationTrigger.class, new NotificationTriggerAdapter())
                .registerTypeHierarchyAdapter(NotificationType.class, new NotificationTypeAdapter())
                .registerTypeHierarchyAdapter(Sound.class, new SoundAdapter())
                .registerTypeHierarchyAdapter(Sound.Source.class, new EnumAdapter<>(Sound.Source.class))
                .registerTypeHierarchyAdapter(UnparsedTitle.class, new UnparsedTitleAdapter())
                .registerTypeHierarchyAdapter(Title.Times.class, new TitleTimesAdapter())
                .build();
    }

    public void connect(final Player player, final String server) {
        final var dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("Connect");
        dataOutput.writeUTF(server);
        player.sendPluginMessage(this, "BungeeCord", dataOutput.toByteArray());
    }
}
