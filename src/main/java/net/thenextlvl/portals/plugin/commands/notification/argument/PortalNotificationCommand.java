package net.thenextlvl.portals.plugin.commands.notification.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class PortalNotificationCommand<T> extends SimpleCommand {
    private final NotificationType<T> notificationType;

    protected PortalNotificationCommand(final PortalsPlugin plugin, final NotificationType<T> notificationType, final String name) {
        super(plugin, name, null);
        this.notificationType = notificationType;
    }

    protected int addAction(final CommandContext<CommandSourceStack> context, final T input) {
        final var sender = context.getSource().getSender();
        final var portal = context.getArgument("portal", Portal.class);
        final var trigger = context.getArgument("trigger", NotificationTrigger.class);

        final var success = portal.getNotifications().set(trigger, notificationType, input);

        final var message = success ? "portal.notification.added" : "nothing.changed";
        plugin.bundle().sendMessage(sender, message,
                Placeholder.unparsed("type", notificationType.getName()),
                Placeholder.unparsed("trigger", trigger.getName()),
                Placeholder.unparsed("portal", portal.getName()));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
