package net.thenextlvl.portals.plugin.model;

import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.notification.Notification;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.notification.NotificationType;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SimpleNotification<T>(
        NotificationTrigger trigger,
        NotificationType<T> type,
        T input
) implements Notification<T> {
    @Override
    public void send(final Entity entity, final Portal portal) {
        type.getSender().send(entity, portal, input);
    }
}
