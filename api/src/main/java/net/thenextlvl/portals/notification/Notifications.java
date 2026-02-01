package net.thenextlvl.portals.notification;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a collection of notifications.
 *
 * @since 1.4.0
 */
@ApiStatus.NonExtendable
public interface Notifications extends Iterable<Notification<?>> {
    /**
     * Gets all notifications.
     *
     * @return a stream of all notifications
     * @since 1.4.0
     */
    Stream<Notification<?>> stream();

    /**
     * Gets all notifications for the given trigger.
     *
     * @param trigger the trigger to get the notifications for
     * @return a stream of all notifications for the given trigger
     * @since 1.4.0
     */
    Stream<Notification<?>> findByTrigger(NotificationTrigger trigger);

    /**
     * Checks if there are any notifications.
     *
     * @return {@code true} if there are no notifications, {@code false} otherwise
     * @since 1.4.0
     */
    boolean isEmpty();

    /**
     * Gets the number of notifications.
     *
     * @return the number of notifications
     * @since 1.4.0
     */
    int size();

    /**
     * Sends all notifications for the given trigger to the specified entity.
     *
     * @param trigger the notification trigger
     * @param entity  the entity to send the notifications to
     * @since 1.4.0
     */
    void trigger(NotificationTrigger trigger, Entity entity);

    /**
     * Gets the notification for a specific trigger and type.
     *
     * @param trigger the trigger to get the notification for
     * @param type    the notification type
     * @return the notification or an empty optional if not found
     * @since 1.4.0
     */
    @Contract(pure = true)
    <T> Optional<T> get(NotificationTrigger trigger, NotificationType<T> type);

    /**
     * Sets a notification for a specific trigger and type.
     *
     * @param trigger the trigger to set the notification for
     * @param type    the notification type
     * @param input   the input for the notification
     * @return {@code true} if anything was changed, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(mutates = "this")
    <T> boolean set(NotificationTrigger trigger, NotificationType<T> type, T input);

    /**
     * Removes a notification for a specific trigger.
     *
     * @param trigger the trigger to remove the notification from
     * @param type    the notification type to remove
     * @return {@code true} if the notification was removed, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(mutates = "this")
    boolean remove(NotificationTrigger trigger, NotificationType<?> type);
}
