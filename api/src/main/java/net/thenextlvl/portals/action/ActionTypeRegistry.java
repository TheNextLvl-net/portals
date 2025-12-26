package net.thenextlvl.portals.action;

import net.thenextlvl.binder.StaticBinder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a registry of action types.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface ActionTypeRegistry {
    /**
     * Gets the action type registry.
     *
     * @return the action type registry
     * @since 0.1.0
     */
    static @CheckReturnValue ActionTypeRegistry registry() {
        return StaticBinder.getInstance(ActionTypeRegistry.class.getClassLoader()).find(ActionTypeRegistry.class);
    }

    /**
     * Registers an action type.
     *
     * @param type the action type to register
     * @return {@code true} if the action type was registered, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean register(ActionType<?> type);

    /**
     * Checks if an action type is registered.
     *
     * @param type the action type to check
     * @return {@code true} if the action type is registered, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean isRegistered(ActionType<?> type);

    /**
     * Checks if an action type is registered.
     *
     * @param name the name of the action type to check
     * @return {@code true} if the action type is registered, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean isRegistered(String name);

    /**
     * Unregisters an action type.
     *
     * @param type the action type to unregister
     * @return {@code true} if the action type was unregistered, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean unregister(ActionType<?> type);

    /**
     * Gets an action type by name.
     *
     * @param name the name of the action type
     * @param <T>  the type of input required by the action type
     * @return the action type, or an empty optional if not found
     * @since 0.1.0
     */
    @Contract(pure = true)
    <T> Optional<ActionType<T>> getByName(String name);

    /**
     * Gets all action types.
     *
     * @return an unmodifiable set of all action types
     * @since 0.1.0
     */
    @Unmodifiable
    @Contract(pure = true)
    Set<ActionType<?>> getActionTypes();
}
