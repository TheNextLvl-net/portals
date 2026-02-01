package net.thenextlvl.portals.notification;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;

import java.util.Objects;

final class SimpleNotification<T> implements Notification<T> {
    private final NotificationType<T> type;
    private T input;

    public SimpleNotification(final NotificationType<T> type, final T input) {
        this.type = type;
        this.input = input;
    }

    @Override
    public NotificationType<T> getNotificationType() {
        return type;
    }

    @Override
    public T getInput() {
        return input;
    }

    @Override
    public boolean setInput(final T input) {
        if (Objects.equals(this.input, input)) return false;
        this.input = input;
        return true;
    }

    @Override
    public void send(final Entity entity, final Portal portal) {
        type.getSender().send(entity, portal, input);
    }
}
