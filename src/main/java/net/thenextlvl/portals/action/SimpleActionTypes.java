package net.thenextlvl.portals.action;

import core.paper.messenger.PluginMessenger;
import io.papermc.paper.entity.TeleportFlag;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.listener.PortalListener;
import net.thenextlvl.portals.bounds.Bounds;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

@NullMarked
public final class SimpleActionTypes implements ActionTypes {
    public static final SimpleActionTypes INSTANCE = new SimpleActionTypes();
    
    private final PortalsPlugin plugin = JavaPlugin.getPlugin(PortalsPlugin.class);
    private final PluginMessenger messenger = new PluginMessenger(plugin);

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
            var sourceBB = portal.getBoundingBox();
            var targetBB = targetPortal.getBoundingBox();
            var from = entity.getLocation();

            // Source extents
            var sMin = new double[]{sourceBB.getMinX(), sourceBB.getMinY(), sourceBB.getMinZ()};
            var sMax = new double[]{sourceBB.getMaxX(), sourceBB.getMaxY(), sourceBB.getMaxZ()};
            var sSize = new double[]{sMax[0] - sMin[0], sMax[1] - sMin[1], sMax[2] - sMin[2]};

            // Identify source axis roles by size:
            // W = thickness (smallest), U = largest (width), V = middle (height)
            var sW = 0;
            if (sSize[1] < sSize[sW]) sW = 1;
            if (sSize[2] < sSize[sW]) sW = 2;
            var sU = (sW == 0) ? 1 : 0;
            var sV = (sW == 2) ? 1 : 2;
            if (sSize[sV] > sSize[sU]) {
                var tmp = sU;
                sU = sV;
                sV = tmp;
            }

            // Fractions inside source portal along U/V/W
            var eps = 1.0e-6;
            var margin = 1.0e-3; // keep slightly inside to avoid edge issues

            var pos = new double[]{from.getX(), from.getY(), from.getZ()};
            var fu = (sSize[sU] < eps) ? 0 : (pos[sU] - sMin[sU]) / Math.max(sSize[sU], eps);
            var fv = (sSize[sV] < eps) ? 0 : (pos[sV] - sMin[sV]) / Math.max(sSize[sV], eps);
            var fw = (sSize[sW] < eps) ? 0 : (pos[sW] - sMin[sW]) / Math.max(sSize[sW], eps);

            // Clamp to always be strictly inside
            fu = Math.min(1 - margin, Math.max(margin, fu));
            // fv = Math.min(1 - margin, Math.max(margin, fv));
            fw = Math.min(1 - margin, Math.max(margin, fw));

            // Target extents
            var tMin = new double[]{targetBB.getMinX(), targetBB.getMinY(), targetBB.getMinZ()};
            var tMax = new double[]{targetBB.getMaxX(), targetBB.getMaxY(), targetBB.getMaxZ()};
            var tSize = new double[]{tMax[0] - tMin[0], tMax[1] - tMin[1], tMax[2] - tMin[2]};

            // Identify target axis roles by size (match roles, not absolute axes)
            var tW = 0;
            if (tSize[1] < tSize[tW]) tW = 1;
            if (tSize[2] < tSize[tW]) tW = 2;
            var tU = (tW == 0) ? 1 : 0;
            var tV = (tW == 2) ? 1 : 2;
            if (tSize[tV] > tSize[tU]) {
                var tmp = tU;
                tU = tV;
                tV = tmp;
            }

            // Map U/V/W fractions from source to target roles
            var dest = new double[3];
            dest[tU] = tMin[tU] + fu * tSize[tU];
            dest[tV] = tMin[tV] + fv * tSize[tV];
            dest[tW] = tMin[tW] + fw * tSize[tW];

            var destination = new Location(targetPortal.getWorld(), dest[0], dest[1], dest[2]).setRotation(from.getRotation());
            entity.teleportAsync(destination, PlayerTeleportEvent.TeleportCause.PLUGIN, TeleportFlag.Relative.values()).thenAccept(success -> {
                if (success) PortalListener.setLastPortal(entity, targetPortal);
            });
            return true;

        }).orElse(false);
    });

    private final ActionType<Bounds> teleportRandom = ActionType.create("teleport_random", Bounds.class, (entity, portal, bounds) -> {
        bounds.searchSafeLocation(ThreadLocalRandom.current()).thenAccept(location -> {
            if (location == null) {
                System.out.println("Failed to find a safe location within bounds: " + bounds);
                // todo: send message to player
                // plugin.bundle().sendMessage(entity, "portal.action.teleport-random.failed");
                return;
            }
            location.setRotation(entity.getLocation().getRotation());
            entity.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            System.out.println("random teleported to " + location);
        });
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

