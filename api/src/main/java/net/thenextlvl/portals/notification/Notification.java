package net.thenextlvl.portals.notification;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

public interface Notification<T> {
    @Contract(pure = true)
    NotificationTrigger trigger();

    @Contract(pure = true)
    NotificationType<T> type();

    @Contract(pure = true)
    T input();

    void send(Entity entity, Portal portal);
}
