package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents an action to be performed when an entity enters a portal.
 *
 * @since 0.1.0
 */
public sealed interface EntryAction<T> permits SimpleEntryAction {
    /**
     * Gets the action type.
     *
     * @return the action type
     * @since 0.1.0
     */
    @Contract(pure = true)
    ActionType<T> getActionType();

    /**
     * Gets the input for the action.
     *
     * @return the input
     * @since 0.1.0
     */
    @Contract(pure = true)
    T getInput();

    /**
     * Sets the input for the action.
     *
     * @param input the input
     * @return {@code true} if the input was set, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean setInput(T input);

    /**
     * Performs the action.
     *
     * @param entity the entity that entered the portal
     * @param portal the portal that was entered
     * @return {@code true} if the action was successful, {@code false} otherwise
     * @since 0.1.0
     */
    boolean onEntry(Entity entity, Portal portal);

    /**
     * Creates a new entry action.
     *
     * @param type  the action type
     * @param input the input for the action
     * @param <T>   the type of input required by the action
     * @return the new entry action
     * @since 0.1.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> EntryAction<T> create(final ActionType<T> type, final T input) {
        return new SimpleEntryAction<>(type, input);
    }
}
