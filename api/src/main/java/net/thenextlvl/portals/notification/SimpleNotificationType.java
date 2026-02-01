package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;

record SimpleNotificationType<T>(
        @KeyPattern.Value String name,
        Class<T> type,
        Sender<T> sender
) implements NotificationType<T> {
    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public @KeyPattern.Value String getName() {
        return name;
    }

    @Override
    public Sender<T> getSender() {
        return sender;
    }
}
