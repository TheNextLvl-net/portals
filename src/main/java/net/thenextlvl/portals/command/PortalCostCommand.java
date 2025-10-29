package net.thenextlvl.portals.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.command.PortalCommand.portalArgument;

@NullMarked
final class PortalCostCommand extends SimpleCommand {
    public PortalCostCommand(PortalsPlugin plugin) {
        super(plugin, "cost", "portals.command.cost");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalCostCommand(plugin);
        var cost = Commands.argument("cost", DoubleArgumentType.doubleArg(0));
        return command.create().then(portalArgument(plugin)
                .then(cost.executes(command))
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();

        var portal = context.getArgument("portal", Portal.class);
        var cost = tryGetArgument(context, "cost", Double.class).orElse(null);
        var message = cost == null ? "portal.cost.current"
                : cost.equals(portal.getEntryCost()) ? "nothing.changed" : "portal.cost.set";
        if (cost != null) portal.setEntryCost(cost);

        var formatted = plugin.economyProvider().format(sender, cost != null ? cost : portal.getEntryCost());
        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("cost", formatted));

        return SINGLE_SUCCESS;
    }
}