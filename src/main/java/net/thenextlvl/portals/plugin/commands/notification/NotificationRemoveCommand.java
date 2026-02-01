package net.thenextlvl.portals.plugin.commands.notification;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.notification.Notification;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.NotificationTriggerArgumentType;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import net.thenextlvl.portals.plugin.commands.suggestions.PortalNotificationSuggestionProvider;
import net.thenextlvl.portals.plugin.commands.suggestions.PortalNotificationTriggerSuggestionProvider;
import net.thenextlvl.portals.plugin.commands.suggestions.PortalWithNotificationSuggestionProvider;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class NotificationRemoveCommand extends SimpleCommand {
    private NotificationRemoveCommand(final PortalsPlugin plugin) {
        super(plugin, "remove", "portals.command.notification.remove");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new NotificationRemoveCommand(plugin);
        final var trigger = Commands.argument("trigger", new NotificationTriggerArgumentType())
                .suggests(new PortalNotificationTriggerSuggestionProvider());
        final var notification = Commands.argument("notification", StringArgumentType.word())
                .suggests(new PortalNotificationSuggestionProvider());
        return command.create().then(portalArgument(plugin)
                .suggests(new PortalWithNotificationSuggestionProvider<>())
                .then(trigger.then(notification.executes(command))));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var portal = context.getArgument("portal", Portal.class);
        final var trigger = context.getArgument("trigger", NotificationTrigger.class);
        final var notification = context.getArgument("notification", String.class);

        final var success = portal.getNotifications().findByTrigger(trigger)
                .map(Notification::type)
                .filter(type -> type.getName().equals(notification))
                .findAny()
                .map(n -> portal.getNotifications().remove(trigger, n))
                .orElse(false);
        final var message = success ? "portal.notification.removed" : "nothing.changed";

        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("trigger", trigger.getName()),
                Placeholder.parsed("type", notification));

        return success ? SINGLE_SUCCESS : 0;
    }
}
