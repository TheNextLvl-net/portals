package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.CurrencyArgumentType;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class PortalCostCommand extends SimpleCommand {
    public PortalCostCommand(final PortalsPlugin plugin) {
        super(plugin, "cost", "portals.command.cost");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalCostCommand(plugin);
        final var cost = Commands.argument("cost", DoubleArgumentType.doubleArg(0));
        final var currency = Commands.argument("currency", new CurrencyArgumentType(plugin));
        return command.create().then(portalArgument(plugin)
                .then(cost.executes(command).then(currency.executes(command)))
                .executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();

        final var portal = context.getArgument("portal", Portal.class);
        final var cost = tryGetArgument(context, "cost", Double.class).orElse(null);
        final var currency = tryGetArgument(context, "currency", String.class).orElse(null);

        final var format = plugin.economyProvider().format(sender, portal.getCurrency().orElse(null), cost != null ? cost : portal.getEntryCost());
        if (cost == null) {
            plugin.bundle().sendMessage(sender, "portal.cost.current",
                    Placeholder.component("cost", format),
                    Placeholder.parsed("portal", portal.getName()));
            return SINGLE_SUCCESS;
        }

        final var success = portal.setEntryCost(cost) | portal.setCurrency(currency);
        final var message = success ? "portal.cost.set" : "nothing.changed";

        plugin.bundle().sendMessage(sender, message,
                Placeholder.component("cost", format),
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}