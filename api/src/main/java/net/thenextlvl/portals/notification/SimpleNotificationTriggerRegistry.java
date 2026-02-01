package net.thenextlvl.portals.notification;

import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

final class SimpleNotificationTriggerRegistry implements NotificationTriggerRegistry {
    public static final SimpleNotificationTriggerRegistry INSTANCE = new SimpleNotificationTriggerRegistry();

    private final Set<NotificationTrigger> triggers = new HashSet<>(Set.of(
            NotificationTrigger.teleportSuccess(),
            NotificationTrigger.teleportFailure(),
            NotificationTrigger.entrySuccess(),
            NotificationTrigger.entryFailure(),
            NotificationTrigger.exit()
    ));

    @Override
    public boolean register(final NotificationTrigger trigger) {
        return triggers.add(trigger);
    }

    @Override
    public boolean isRegistered(final NotificationTrigger trigger) {
        return triggers.contains(trigger);
    }

    @Override
    public boolean isRegistered(final String name) {
        return triggers.stream().anyMatch(trigger -> trigger.getName().equals(name));
    }

    @Override
    public boolean unregister(final NotificationTrigger trigger) {
        return triggers.remove(trigger);
    }

    @Override
    public Optional<NotificationTrigger> getByName(final String name) {
        return triggers.stream()
                .filter(trigger -> trigger.getName().equals(name))
                .findAny();
    }

    @Override
    public @Unmodifiable Set<NotificationTrigger> getNotificationTriggers() {
        return Set.copyOf(triggers);
    }
}
