package net.thenextlvl.portals.plugin.commands.notification.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class PortalStringNotificationCommand extends PortalNotificationCommand<String> {
    private final String argument;

    protected PortalStringNotificationCommand(final PortalsPlugin plugin, final NotificationType<String> actionType, final String argument) {
        super(plugin, actionType);
        this.argument = argument;
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> create() {
        final var argument = Commands.argument(this.argument, StringArgumentType.greedyString());
        return super.create().then(argument.executes(this));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        return addAction(context, context.getArgument(argument, String.class));
    }
}
