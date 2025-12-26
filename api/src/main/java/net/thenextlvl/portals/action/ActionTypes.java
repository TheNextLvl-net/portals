package net.thenextlvl.portals.action;

import net.thenextlvl.binder.StaticBinder;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.bounds.Bounds;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;

import java.net.InetSocketAddress;

/**
 * Represents a registry of action types.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface ActionTypes {
    /**
     * Gets the action type registry.
     *
     * @return the action type registry
     * @since 0.1.0
     */
    static @CheckReturnValue ActionTypes types() {
        return StaticBinder.getInstance(ActionTypes.class.getClassLoader()).find(ActionTypes.class);
    }

    /**
     * Gets the action type for connecting a player to another server (within the network).
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<String> connect();

    /**
     * Gets the action type for running a command for a player.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<String> runCommand();

    /**
     * Gets the action type for running a console command.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<String> runConsoleCommand();

    /**
     * Gets the action type for teleporting an entity.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<Location> teleport();

    /**
     * Gets the action type for teleporting an entity to another portal.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<PortalLike> teleportPortal();

    /**
     * Gets the action type for teleporting an entity to a random location within certain bounds.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<Bounds> teleportRandom();

    /**
     * Gets the action type for transferring a player to another server.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<InetSocketAddress> transfer();
}
