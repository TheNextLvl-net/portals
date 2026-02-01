package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.view.UnparsedTitle;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents a type of notification that can be sent to an entity.
 *
 * @param <T> the type of input required by the notification
 * @since 1.4.0
 */
public sealed interface NotificationType<T> permits SimpleNotificationType {
    /**
     * Gets the notification type for sending a sound to an entity.
     *
     * @return the sound notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationType<Sound> sound() {
        return SimpleNotificationType.PLAY_SOUND;
    }

    /**
     * Gets the notification type for sending a chat message to an entity.
     *
     * @return the message notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationType<String> message() {
        return SimpleNotificationType.MESSAGE;
    }

    /**
     * Gets the notification type for sending a title to an entity.
     *
     * @return the title notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationType<UnparsedTitle> title() {
        return SimpleNotificationType.TITLE;
    }

    /**
     * Gets the notification type for sending an action bar to an entity.
     *
     * @return the action bar notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationType<String> actionbar() {
        return SimpleNotificationType.ACTIONBAR;
    }

    /**
     * Gets the type of the input for this notification.
     *
     * @return the type of the input
     * @since 1.4.0
     */
    @Contract(pure = true)
    Class<T> getType();

    /**
     * Gets the name of this notification type.
     *
     * @return the name of this notification type
     * @since 1.4.0
     */
    @KeyPattern.Value
    @Contract(pure = true)
    String getName();

    /**
     * Gets the sender used to send this notification type.
     *
     * @return the sender
     * @since 1.4.0
     */
    @Contract(pure = true)
    Sender<T> getSender();

    /**
     * Represents a sender that sends a notification to an entity.
     *
     * @param <T> the type of input required by the notification
     * @since 1.4.0
     */
    @FunctionalInterface
    interface Sender<T> {
        /**
         * Sends the notification to the entity.
         *
         * @param entity the entity to send the notification to
         * @param portal the portal that triggered the notification
         * @param input  the input for the notification
         * @since 1.4.0
         */
        void send(Entity entity, Portal portal, T input);
    }

    /**
     * Creates a new notification type.
     *
     * @param name   the name of the notification type
     * @param type   the type of the input for the notification
     * @param sender the sender used to send the notification
     * @param <T>    the type of input required by the notification
     * @return the new notification type
     * @since 1.4.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static <T> NotificationType<T> create(
            @KeyPattern.Value final String name,
            final Class<T> type,
            final Sender<T> sender
    ) {
        return new SimpleNotificationType<>(name, type, sender);
    }
}
