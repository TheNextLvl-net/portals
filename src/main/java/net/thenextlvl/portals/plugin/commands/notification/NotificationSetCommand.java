package net.thenextlvl.portals.plugin.commands.notification;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.NotificationTriggerArgumentType;
import net.thenextlvl.portals.plugin.commands.brigadier.BrigadierCommand;
import net.thenextlvl.portals.plugin.commands.notification.argument.PlaySoundCommand;
import net.thenextlvl.portals.plugin.commands.notification.argument.SendActionbarCommand;
import net.thenextlvl.portals.plugin.commands.notification.argument.SendMessageCommand;
import net.thenextlvl.portals.plugin.commands.notification.argument.SendTitleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
public final class NotificationSetCommand extends BrigadierCommand {
    private NotificationSetCommand(final PortalsPlugin plugin) {
        super(plugin, "set", "portals.command.notification.set");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new NotificationSetCommand(plugin);
        final var trigger = Commands.argument("trigger", new NotificationTriggerArgumentType());
        return command.create().then(portalArgument(plugin).then(trigger
                .then(PlaySoundCommand.create(plugin))
                .then(SendActionbarCommand.create(plugin))
                .then(SendMessageCommand.create(plugin))
                .then(SendTitleCommand.create(plugin))));
    }
}
