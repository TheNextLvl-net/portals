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
