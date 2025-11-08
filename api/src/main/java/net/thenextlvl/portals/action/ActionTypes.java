package net.thenextlvl.portals.action;

import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.model.Bounds;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;

import java.net.InetSocketAddress;

/**
 * @since 0.1.0
 */
public sealed interface ActionTypes permits SimpleActionTypes {
    @Contract(pure = true)
    static ActionTypes types() {
        return SimpleActionTypes.INSTANCE;
    }

    @Contract(pure = true)
    ActionType<String> connect();

    @Contract(pure = true)
    ActionType<String> runCommand();

    @Contract(pure = true)
    ActionType<String> runConsoleCommand();

    @Contract(pure = true)
    ActionType<Location> teleport();

    @Contract(pure = true)
    ActionType<PortalLike> teleportPortal();

    @Contract(pure = true)
    ActionType<Bounds> teleportRandom();

    @Contract(pure = true)
    ActionType<InetSocketAddress> transfer();
}
