package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import org.jspecify.annotations.NullMarked;

import java.net.InetSocketAddress;

@NullMarked
public final class TransferCommand extends ActionCommand<InetSocketAddress> {
    private TransferCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().transfer(), "transfer");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new TransferCommand(plugin);
        var hostname = Commands.argument("hostname", StringArgumentType.string()).executes(command);
        var port = Commands.argument("port", IntegerArgumentType.integer(1, 65535)).executes(command);
        return command.create().then(hostname.then(port));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var hostname = context.getArgument("hostname", String.class);
        var port = tryGetArgument(context, "port", int.class).orElse(25565);
        return addAction(context, new InetSocketAddress(hostname, port));
    }
}
