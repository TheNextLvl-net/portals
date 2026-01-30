package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import org.jspecify.annotations.NullMarked;

import java.net.InetSocketAddress;

@NullMarked
public final class TransferCommand extends ActionCommand<InetSocketAddress> {
    private TransferCommand(final PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().transfer(), "transfer");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new TransferCommand(plugin);
        final var hostname = Commands.argument("hostname", StringArgumentType.string()).executes(command);
        final var port = Commands.argument("port", IntegerArgumentType.integer(1, 65535)).executes(command);
        return command.create().then(hostname.then(port));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var hostname = context.getArgument("hostname", String.class);
        final var port = tryGetArgument(context, "port", int.class).orElse(25565);
        return addAction(context, new InetSocketAddress(hostname, port));
    }

    @Override
    protected void onSuccess(final CommandContext<CommandSourceStack> context, final Portal portal, final InetSocketAddress input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.transfer",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("hostname", input.getHostString()),
                Placeholder.parsed("port", String.valueOf(input.getPort())));
    }
}
