package net.thenextlvl.portals.notification;

import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.view.UnparsedTitle;
import org.bukkit.entity.Entity;

record SimpleNotificationType<T>(
        @KeyPattern.Value String name,
        Class<T> type,
        Sender<T> sender
) implements NotificationType<T> {
    public static final NotificationType<Sound> PLAY_SOUND = NotificationType.create("sound", Sound.class, (entity, portal, input) -> {
        entity.playSound(input);
    });

    public static final NotificationType<String> MESSAGE = NotificationType.create("message", String.class, (entity, portal, input) -> {
        entity.sendMessage(MiniMessage.miniMessage().deserialize(input, placeholders(entity, portal)));
    });

    public static final NotificationType<UnparsedTitle> TITLE = NotificationType.create("title", UnparsedTitle.class, (entity, portal, input) -> {
        var miniMessage = MiniMessage.miniMessage();
        var placeholders = placeholders(entity, portal);
        var title = miniMessage.deserialize(input.title(), placeholders);
        var subtitle = miniMessage.deserialize(input.subtitle(), placeholders);
        entity.showTitle(Title.title(title, subtitle, input.times()));
    });

    public static final NotificationType<String> ACTIONBAR = NotificationType.create("actionbar", String.class, (entity, portal, input) -> {
        entity.sendActionBar(MiniMessage.miniMessage().deserialize(input, placeholders(entity, portal)));
    });

    private static TagResolver[] placeholders(final Entity entity, final Portal portal) {
        return new TagResolver[]{
                Formatter.number("cooldown", portal.getCooldown().toMillis() / 1000d),
                Formatter.number("remaining-cooldown", portal.getRemainingCooldown(entity).toMillis() / 1000d),
                Formatter.number("remaining-warmup", portal.getRemainingWarmup(entity).toMillis() / 1000d),
                Formatter.number("warmup", portal.getWarmup().toMillis() / 1000d),
                Placeholder.parsed("entity", entity.getName()),
                Placeholder.parsed("entry-cost", portal.getFormattedEntryCost(entity)),
                Placeholder.parsed("permission", portal.getEntryPermission().orElse("undefined")),
                Placeholder.parsed("portal", portal.getName())
        };
    }

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
