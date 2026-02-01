package net.thenextlvl.portals.notification;

import org.jetbrains.annotations.Contract;

/**
 * Provides access to built-in notification triggers.
 *
 * @since 1.3.0
 */
public sealed interface NotificationTriggers permits SimpleNotificationTriggers {
    /**
     * Gets the notification triggers instance.
     *
     * @return the notification triggers instance
     * @since 1.3.0
     */
    @Contract(pure = true)
    static NotificationTriggers triggers() {
        return SimpleNotificationTriggers.INSTANCE;
    }

    /**
     * Gets the trigger for successful teleportation.
     *
     * @return the teleport success trigger
     * @since 1.3.0
     */
    @Contract(pure = true)
    NotificationTrigger teleportSuccess();

    /**
     * Gets the trigger for failed teleportation.
     *
     * @return the teleport failure trigger
     * @since 1.3.0
     */
    @Contract(pure = true)
    NotificationTrigger teleportFailure();

    /**
     * Gets the trigger for successful portal exit.
     *
     * @return the exit success trigger
     * @since 1.3.0
     */
    @Contract(pure = true)
    NotificationTrigger exitSuccess();

    /**
     * Gets the trigger for failed portal exit.
     *
     * @return the exit failure trigger
     * @since 1.3.0
     */
    @Contract(pure = true)
    NotificationTrigger exitFailure();
}
