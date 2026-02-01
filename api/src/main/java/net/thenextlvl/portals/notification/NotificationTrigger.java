package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;
import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents a trigger that determines when and how notifications are sent.
 *
 * @since 1.3.0
 */
public sealed interface NotificationTrigger permits SimpleNotificationTrigger {
    /**
     * Gets the name of this trigger.
     *
     * @return the name of this trigger
     * @since 1.3.0
     */
    @KeyPattern.Value
    @Contract(pure = true)
    String getName();

    /**
     * Triggers the notifications for the given entity and portal.
     *
     * @param entity the entity to send the notifications to
     * @param portal the portal that called the trigger
     * @since 1.3.0
     */
    void trigger(Entity entity, Portal portal);

    /**
     * Creates a new notification trigger.
     *
     * @param name the name of the trigger
     * @return the new notification trigger
     * @since 1.3.0
     */
    @Contract(value = "_ -> new", pure = true)
    static NotificationTrigger create(@KeyPattern.Value final String name) {
        return new SimpleNotificationTrigger(name);
    }
}
