package net.thenextlvl.portals.notification;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a registry of notification types.
 *
 * @since 1.4.0
 */
public sealed interface NotificationTypeRegistry permits SimpleNotificationTypeRegistry {
    /**
     * Gets the notification type registry.
     *
     * @return the notification type registry
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTypeRegistry registry() {
        return SimpleNotificationTypeRegistry.INSTANCE;
    }

    /**
     * Registers a notification type.
     *
     * @param type the notification type to register
     * @return {@code true} if the notification type was registered, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(mutates = "this")
    boolean register(NotificationType<?> type);

    /**
     * Checks if a notification type is registered.
     *
     * @param type the notification type to check
     * @return {@code true} if the notification type is registered, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(pure = true)
    boolean isRegistered(NotificationType<?> type);

    /**
     * Checks if a notification type is registered.
     *
     * @param name the name of the notification type to check
     * @return {@code true} if the notification type is registered, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(pure = true)
    boolean isRegistered(String name);

    /**
     * Unregisters a notification type.
     *
     * @param type the notification type to unregister
     * @return {@code true} if the notification type was unregistered, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(mutates = "this")
    boolean unregister(NotificationType<?> type);

    /**
     * Gets a notification type by name.
     *
     * @param name the name of the notification type
     * @param <T>  the type of input required by the notification type
     * @return the notification type, or an empty optional if not found
     * @since 1.4.0
     */
    @Contract(pure = true)
    <T> Optional<NotificationType<T>> getByName(String name);

    /**
     * Gets all notification types.
     *
     * @return an unmodifiable set of all notification types
     * @since 1.4.0
     */
    @Unmodifiable
    @Contract(pure = true)
    Set<NotificationType<?>> getNotificationTypes();
}
