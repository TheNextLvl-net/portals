package net.thenextlvl.portals.notification;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

final class SimpleNotificationTypes implements NotificationTypes {
    public static final SimpleNotificationTypes INSTANCE = new SimpleNotificationTypes();

    private final NotificationType<Sound> playSound = NotificationType.create("sound", Sound.class, (entity, portal, input) -> {
        entity.playSound(input);
    });

    private final NotificationType<String> message = NotificationType.create("message", String.class, (entity, portal, input) -> {
        entity.sendRichMessage(input);
    });

    private final NotificationType<Title> title = NotificationType.create("title", Title.class, (entity, portal, input) -> {
        entity.showTitle(input);
    });

    private final NotificationType<String> actionBar = NotificationType.create("actionbar", String.class, (entity, portal, input) -> {
        entity.sendActionBar(MiniMessage.miniMessage().deserialize(input));
    });

    @Override
    public NotificationType<Sound> sound() {
        return playSound;
    }

    @Override
    public NotificationType<String> message() {
        return message;
    }

    @Override
    public NotificationType<Title> title() {
        return title;
    }

    @Override
    public NotificationType<String> actionbar() {
        return actionBar;
    }
}
