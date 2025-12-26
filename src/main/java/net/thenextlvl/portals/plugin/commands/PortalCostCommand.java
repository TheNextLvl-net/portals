package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

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

        if (cost == null) {
            plugin.bundle().sendMessage(sender, "portal.cost.current",
                    Placeholder.parsed("cost", plugin.economyProvider().format(sender, portal.getEntryCost())),
                    Placeholder.parsed("portal", portal.getName()));
            return SINGLE_SUCCESS;
        }

        var success = portal.setEntryCost(cost);
        var message = success ? "portal.cost.set" : "nothing.changed";

        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("cost", plugin.economyProvider().format(sender, cost)),
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}