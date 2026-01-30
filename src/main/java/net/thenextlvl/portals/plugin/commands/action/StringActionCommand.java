package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.action.ActionType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class StringActionCommand extends ActionCommand<String> {
    private final String argument;

    protected StringActionCommand(final PortalsPlugin plugin, final ActionType<String> actionType, final String name, final String argument) {
        super(plugin, actionType, name);
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
