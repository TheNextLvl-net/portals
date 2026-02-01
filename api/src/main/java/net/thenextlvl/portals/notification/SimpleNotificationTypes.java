package net.thenextlvl.portals.notification;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

final class SimpleNotificationTypes implements NotificationTypes {
    public static final SimpleNotificationTypes INSTANCE = new SimpleNotificationTypes();

    private final NotificationType<Sound> sound = NotificationType.create("sound", Sound.class, (entity, portal, input) -> {
        entity.playSound(input);
    });

    private final NotificationType<Component> message = NotificationType.create("message", Component.class, (entity, portal, input) -> {
        entity.sendMessage(input);
    });

    private final NotificationType<Title> title = NotificationType.create("title", Title.class, (entity, portal, input) -> {
        entity.showTitle(input);
    });

    private final NotificationType<Component> actionBar = NotificationType.create("action_bar", Component.class, (entity, portal, input) -> {
        entity.sendActionBar(input);
    });

    @Override
    public NotificationType<Sound> sound() {
        return sound;
    }

    @Override
    public NotificationType<Component> message() {
        return message;
    }

    @Override
    public NotificationType<Title> title() {
        return title;
    }

    @Override
    public NotificationType<Component> actionBar() {
        return actionBar;
    }
}
