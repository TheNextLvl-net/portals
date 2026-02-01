package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;

record SimpleNotificationTrigger(
        @KeyPattern.Value String name
) implements NotificationTrigger {
    public static final NotificationTrigger TELEPORT_SUCCESS = NotificationTrigger.create("teleport_success");
    public static final NotificationTrigger TELEPORT_FAILURE = NotificationTrigger.create("teleport_failure");
    public static final NotificationTrigger WARMUP_SUCCESS = NotificationTrigger.create("warmup_success");
    public static final NotificationTrigger WARMUP_FAILURE = NotificationTrigger.create("warmup_failure");
    public static final NotificationTrigger ENTRY_SUCCESS = NotificationTrigger.create("entry_success");
    public static final NotificationTrigger ENTRY_FAILURE = NotificationTrigger.create("entry_failure");
    public static final NotificationTrigger EXIT = NotificationTrigger.create("exit");

    @Override
    public @KeyPattern.Value String getName() {
        return name;
    }
}
