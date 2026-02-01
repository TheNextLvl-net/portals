package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;

record SimpleNotificationTrigger(
        @KeyPattern.Value String name
) implements NotificationTrigger {
    @Override
    public @KeyPattern.Value String getName() {
        return name;
    }
}
