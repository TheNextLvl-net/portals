package net.thenextlvl.portals.notification;

final class SimpleNotificationTriggers implements NotificationTriggers {
    public static final SimpleNotificationTriggers INSTANCE = new SimpleNotificationTriggers();

    private final NotificationTrigger teleportSuccess = NotificationTrigger.create("teleport_success");
    private final NotificationTrigger teleportFailure = NotificationTrigger.create("teleport_failure");
    private final NotificationTrigger exitSuccess = NotificationTrigger.create("exit_success");
    private final NotificationTrigger exitFailure = NotificationTrigger.create("exit_failure");

    @Override
    public NotificationTrigger teleportSuccess() {
        return teleportSuccess;
    }

    @Override
    public NotificationTrigger teleportFailure() {
        return teleportFailure;
    }

    @Override
    public NotificationTrigger exitSuccess() {
        return exitSuccess;
    }

    @Override
    public NotificationTrigger exitFailure() {
        return exitFailure;
    }
}
