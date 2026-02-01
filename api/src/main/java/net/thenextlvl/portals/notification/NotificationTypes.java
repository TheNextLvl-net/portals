package net.thenextlvl.portals.notification;

import net.kyori.adventure.sound.Sound;
import net.thenextlvl.portals.view.UnparsedTitle;
import org.jetbrains.annotations.Contract;

/**
 * Provides access to built-in notification types.
 *
 * @since 1.4.0
 */
public sealed interface NotificationTypes permits SimpleNotificationTypes {
    /**
     * Gets the notification types instance.
     *
     * @return the notification types instance
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTypes types() {
        return SimpleNotificationTypes.INSTANCE;
    }

    /**
     * Gets the notification type for sending a sound to an entity.
     *
     * @return the sound notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationType<Sound> sound();

    /**
     * Gets the notification type for sending a chat message to an entity.
     *
     * @return the message notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationType<String> message();

    /**
     * Gets the notification type for sending a title to an entity.
     *
     * @return the title notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationType<UnparsedTitle> title();

    /**
     * Gets the notification type for sending an action bar to an entity.
     *
     * @return the action bar notification type
     * @since 1.4.0
     */
    @Contract(pure = true)
    NotificationType<String> actionbar();
}
