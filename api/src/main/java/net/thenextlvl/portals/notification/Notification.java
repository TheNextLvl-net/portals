package net.thenextlvl.portals.notification;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents a notification.
 *
 * @param <T> the type of the notification
 * @since 1.4.0
 */
public interface Notification<T> {
    /**
     * Gets the trigger for this notification.
     *
     * @return the trigger for this notification
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationTrigger trigger();

    /**
     * Gets the type of this notification.
     *
     * @return the type of this notification
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationType<T> type();

    /**
     * Gets the input for this notification.
     *
     * @return the input for this notification
     * @since 1.4.0
     */
    @Contract(pure = true)
    T input();

    /**
     * Sends this notification to the given entity.
     *
     * @param entity the entity to send the notification to
     * @param portal the portal that triggered the notification
     * @since 1.4.0
     */
    void send(Entity entity, Portal portal);
}
