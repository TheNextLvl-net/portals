package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;
import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;

record SimpleNotificationTrigger(
        @KeyPattern.Value String name
) implements NotificationTrigger {
    @Override
    public @KeyPattern.Value String getName() {
        return name;
    }

    @Override
    public void trigger(final Entity entity, final Portal portal) {
        portal.getNotifications(this).forEach(notification -> notification.send(entity, portal));
    }
}
