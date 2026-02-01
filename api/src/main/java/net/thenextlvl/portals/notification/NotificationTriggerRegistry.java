package net.thenextlvl.portals.notification;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a registry of notification triggers.
 *
 * @since 1.3.0
 */
public sealed interface NotificationTriggerRegistry permits SimpleNotificationTriggerRegistry {
    /**
     * Gets the notification trigger registry.
     *
     * @return the notification trigger registry
     * @since 1.3.0
     */
    @Contract(pure = true)
    static NotificationTriggerRegistry registry() {
        return SimpleNotificationTriggerRegistry.INSTANCE;
    }

    /**
     * Registers a notification trigger.
     *
     * @param trigger the notification trigger to register
     * @return {@code true} if the notification trigger was registered, {@code false} otherwise
     * @since 1.3.0
     */
    @Contract(mutates = "this")
    boolean register(NotificationTrigger trigger);

    /**
     * Checks if a notification trigger is registered.
     *
     * @param trigger the notification trigger to check
     * @return {@code true} if the notification trigger is registered, {@code false} otherwise
     * @since 1.3.0
     */
    @Contract(pure = true)
    boolean isRegistered(NotificationTrigger trigger);

    /**
     * Checks if a notification trigger is registered.
     *
     * @param name the name of the notification trigger to check
     * @return {@code true} if the notification trigger is registered, {@code false} otherwise
     * @since 1.3.0
     */
    @Contract(pure = true)
    boolean isRegistered(String name);

    /**
     * Unregisters a notification trigger.
     *
     * @param trigger the notification trigger to unregister
     * @return {@code true} if the notification trigger was unregistered, {@code false} otherwise
     * @since 1.3.0
     */
    @Contract(mutates = "this")
    boolean unregister(NotificationTrigger trigger);

    /**
     * Gets a notification trigger by name.
     *
     * @param name the name of the notification trigger
     * @return the notification trigger, or an empty optional if not found
     * @since 1.3.0
     */
    @Contract(pure = true)
    Optional<NotificationTrigger> getByName(String name);

    /**
     * Gets all notification triggers.
     *
     * @return an unmodifiable set of all notification triggers
     * @since 1.3.0
     */
    @Unmodifiable
    @Contract(pure = true)
    Set<NotificationTrigger> getNotificationTriggers();
}
