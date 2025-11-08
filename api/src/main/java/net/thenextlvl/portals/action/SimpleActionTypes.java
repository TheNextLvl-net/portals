package net.thenextlvl.portals.action;

import core.paper.messenger.PluginMessenger;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.model.Bounds;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

final class SimpleActionTypes implements ActionTypes {
    public static final SimpleActionTypes INSTANCE = new SimpleActionTypes();

    private final PluginMessenger messenger = new PluginMessenger(JavaPlugin.getProvidingPlugin(SimpleActionTypes.class));

    private final ActionType<String> connect = ActionType.create("connect", String.class, (entity, portal, server) -> {
        if (!(entity instanceof Player player)) return false;
        messenger.connect(player, server);
        return true;
    });

    private final ActionType<String> runCommand = ActionType.create("run_command", String.class, (entity, portal, input) -> {
        if (!(entity instanceof Player player)) return false;
        player.performCommand(input.replace("<player>", player.getName()));
        return true;
    });

    private final ActionType<String> runConsoleCommand = ActionType.create("run_console_command", String.class, (entity, portal, input) -> {
        if (!(entity instanceof Player player)) return false;
        var command = input.replace("<player>", player.getName());
        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), command);
        return true;
    });

    private final ActionType<Location> teleport = ActionType.create("teleport", Location.class, (entity, portal, location) -> {
        entity.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    });

    private final ActionType<PortalLike> teleportPortal = ActionType.create("teleport_portal", PortalLike.class, (entity, portal, target) -> {
        return target.getPortal().map(targetPortal -> {
            var location = targetPortal.getBoundingBox().getCenter().setRotation(entity.getLocation().getRotation());
            entity.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }).orElse(false);
    });

    private final ActionType<Bounds> teleportRandom = ActionType.create("teleport_random", Bounds.class, (entity, portal, bounds) -> {
        var random = ThreadLocalRandom.current();
        var location = bounds.getRandomLocation(random).setRotation(entity.getLocation().getRotation());
        entity.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    });

    private final ActionType<InetSocketAddress> transfer = ActionType.create("transfer", InetSocketAddress.class, (entity, portal, address) -> {
        if (!(entity instanceof Player player)) return false;
        player.transfer(address.getHostName(), address.getPort());
        return true;
    });

    @Override
    public ActionType<String> connect() {
        return connect;
    }

    @Override
    public ActionType<String> runCommand() {
        return runCommand;
    }

    @Override
    public ActionType<String> runConsoleCommand() {
        return runConsoleCommand;
    }

    @Override
    public ActionType<Location> teleport() {
        return teleport;
    }

    @Override
    public ActionType<PortalLike> teleportPortal() {
        return teleportPortal;
    }

    @Override
    public ActionType<Bounds> teleportRandom() {
        return teleportRandom;
    }

    @Override
    public ActionType<InetSocketAddress> transfer() {
        return transfer;
    }
}

