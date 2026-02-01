package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.Contract;

/**
 * Represents a trigger that determines when and how notifications are sent.
 *
 * @since 1.4.0
 */
public sealed interface NotificationTrigger permits SimpleNotificationTrigger {
    /**
     * Gets the trigger for successful teleportation.
     *
     * @return the teleport success trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger teleportSuccess() {
        return SimpleNotificationTrigger.TELEPORT_SUCCESS;
    }

    /**
     * Gets the trigger for failed teleportation.
     *
     * @return the teleport failure trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger teleportFailure() {
        return SimpleNotificationTrigger.TELEPORT_FAILURE;
    }

    /**
     * Gets the trigger for successful warmup.
     *
     * @return the warmup success trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger warmupSuccess() {
        return SimpleNotificationTrigger.WARMUP_SUCCESS;
    }

    /**
     * Gets the trigger for failed warmup.
     *
     * @return the warmup failure trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger warmupFailure() {
        return SimpleNotificationTrigger.WARMUP_FAILURE;
    }

    /**
     * Gets the trigger for successful portal entry.
     *
     * @return the entry success trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger entrySuccess() {
        return SimpleNotificationTrigger.ENTRY_SUCCESS;
    }

    /**
     * Gets the trigger for failed portal entry.
     *
     * @return the entry failure trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger entryFailure() {
        return SimpleNotificationTrigger.ENTRY_FAILURE;
    }

    /**
     * Gets the trigger for portal exit.
     *
     * @return the exit trigger
     * @since 1.4.0
     */
    @Contract(pure = true)
    static NotificationTrigger exit() {
        return SimpleNotificationTrigger.EXIT;
    }

    /**
     * Gets the name of this trigger.
     *
     * @return the name of this trigger
     * @since 1.4.0
     */
    @KeyPattern.Value
    @Contract(pure = true)
    String getName();

    /**
     * Creates a new notification trigger.
     *
     * @param name the name of the trigger
     * @return the new notification trigger
     * @since 1.4.0
     */
    @Contract(value = "_ -> new", pure = true)
    static NotificationTrigger create(@KeyPattern.Value final String name) {
        return new SimpleNotificationTrigger(name);
    }
}
