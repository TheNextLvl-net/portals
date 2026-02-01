package net.thenextlvl.portals.notification;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents a notification that can be sent to an entity.
 *
 * @param <T> the type of input required by the notification
 * @since 1.4.0
 */
public sealed interface Notification<T> permits SimpleNotification {
    /**
     * Gets the notification type.
     *
     * @return the notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationType<T> getNotificationType();

    /**
     * Gets the input for the notification.
     *
     * @return the input
     * @since 1.4.0
     */
    @Contract(pure = true)
    T getInput();

    /**
     * Sets the input for the notification.
     *
     * @param input the input
     * @return {@code true} if the input was set, {@code false} otherwise
     * @since 1.4.0
     */
    @Contract(mutates = "this")
    boolean setInput(T input);

    /**
     * Sends the notification to the entity.
     *
     * @param entity the entity to send the notification to
     * @param portal the portal that triggered the notification
     * @since 1.4.0
     */
    void send(Entity entity, Portal portal);

    /**
     * Creates a new notification.
     *
     * @param type  the notification type
     * @param input the input for the notification
     * @param <T>   the type of input required by the notification
     * @return the new notification
     * @since 1.4.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> Notification<T> create(final NotificationType<T> type, final T input) {
        return new SimpleNotification<>(type, input);
    }
}
