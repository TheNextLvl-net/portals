package net.thenextlvl.portals.plugin.commands.notification;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.notification.Notification;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.NotificationTriggerArgumentType;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import net.thenextlvl.portals.plugin.commands.suggestions.PortalNotificationTriggerSuggestionProvider;
import net.thenextlvl.portals.plugin.commands.suggestions.PortalWithNotificationSuggestionProvider;
import net.thenextlvl.portals.view.UnparsedTitle;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class NotificationListCommand extends SimpleCommand {
    private NotificationListCommand(final PortalsPlugin plugin) {
        super(plugin, "list", "portals.command.notification.list");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new NotificationListCommand(plugin);
        final var trigger = Commands.argument("trigger", new NotificationTriggerArgumentType())
                .suggests(new PortalNotificationTriggerSuggestionProvider());
        return command.create().then(portalArgument(plugin)
                .suggests(new PortalWithNotificationSuggestionProvider<>())
                .then(trigger.executes(command))
                .executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var portal = context.getArgument("portal", Portal.class);
        final var trigger = tryGetArgument(context, "trigger", NotificationTrigger.class).orElse(null);

        if (trigger != null) {
            list(portal, trigger, sender);
            return SINGLE_SUCCESS;
        }

        final var notifications = portal.getNotifications();
        if (notifications.isEmpty()) {
            plugin.bundle().sendMessage(sender, "portal.notification.list.empty",
                    Placeholder.parsed("portal", portal.getName()));
            return 0;
        }

        notifications.stream().map(Notification::trigger).distinct()
                .forEach(notification -> list(portal, notification, sender));
        return SINGLE_SUCCESS;
    }

    private void list(final Portal portal, final NotificationTrigger trigger, final CommandSender sender) {
        final var notifications = portal.getNotifications().findByTrigger(trigger)
                .map(notification -> plugin.bundle().component("portal.notification.list.entry", sender,
                        Placeholder.parsed("type", notification.type().getName()),
                        Placeholder.parsed("command", toEditCommand(portal, notification))))
                .toList();

        final var message = notifications.isEmpty()
                ? "portal.notification.list.empty.trigger"
                : "portal.notification.list.trigger";

        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("trigger", trigger.getName()),
                Formatter.number("count", notifications.size()),
                Formatter.joining("notifications", notifications));
    }

    private String toEditCommand(final Portal portal, final Notification<?> notification) {
        return "/portal notification set "
                + portal.getName() + " "
                + notification.trigger().getName() + " "
                + notification.type().getName() + " "
                + toString(notification);
    }

    private static String toString(final Notification<?> notification) {
        return switch (notification.input()) {
            case final UnparsedTitle unparsed -> toString(unparsed);
            case final Sound sound -> toString(sound);
            default -> String.valueOf(notification.input());
        };
    }

    private static String toString(final UnparsedTitle unparsed) {
        final var string = StringArgumentType.escapeIfRequired(unparsed.title()) + " "
                + StringArgumentType.escapeIfRequired(unparsed.subtitle());

        if (unparsed.times() == null) return string;

        final var times = unparsed.times().fadeIn().toMillis() / 1000d + "s "
                + unparsed.times().stay().toMillis() / 1000d + "s "
                + unparsed.times().fadeOut().toMillis() / 1000d + "s";

        return string + " " + times;
    }

    private static String toString(final Sound sound) {
        return sound.name().asString() + " "
                + sound.source().name() + " "
                + sound.volume() + " "
                + sound.pitch();
    }
}
