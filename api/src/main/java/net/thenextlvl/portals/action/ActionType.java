package net.thenextlvl.portals.action;

import net.kyori.adventure.key.KeyPattern;
import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents an action that is performed when an entity enters a portal.
 *
 * @since 0.1.0
 */
public sealed interface ActionType<T> permits SimpleActionType {
    /**
     * Gets the type of the input for this action.
     *
     * @return the type of the input
     * @since 0.1.0
     */
    @Contract(pure = true)
    Class<T> getType();

    /**
     * Gets the name of this action.
     *
     * @return the name of this action
     * @since 0.1.0
     */
    @KeyPattern.Value
    @Contract(pure = true)
    String getName();

    /**
     * Gets the action to be performed when an entity enters a portal.
     *
     * @return the action to be performed
     * @since 0.1.0
     */
    @Contract(pure = true)
    Action<T> getAction();

    /**
     * Represents an action to be performed when an entity enters a portal.
     *
     * @param <T> the type of input required by the action
     * @since 0.1.0
     */
    @FunctionalInterface
    interface Action<T> {
        /**
         * Performs the action.
         *
         * @param entity the entity that entered the portal
         * @param portal the portal that was entered
         * @param input  the input for the action
         * @return {@code true} if the action was successful, {@code false} otherwise
         * @since 0.1.0
         */
        boolean invoke(Entity entity, Portal portal, T input);
    }

    /**
     * Creates a new action type.
     *
     * @param name   the name of the action
     * @param type   the type of the input for the action
     * @param action the action to be performed
     * @param <T>    the type of input required by the action
     * @return the new action type
     * @since 0.1.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static <T> ActionType<T> create(@KeyPattern.Value String name, Class<T> type, Action<T> action) {
        return new SimpleActionType<>(name, type, action);
    }
}
