package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PortalListCommand extends SimpleCommand {
    public PortalListCommand(PortalsPlugin plugin) {
        super(plugin, "list", "portals.command.list");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalListCommand(plugin);
        return command.create()
                .then(Commands.argument("world", ArgumentTypes.world()).executes(command))
                .executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var world = tryGetArgument(context, "world", World.class);
        var portals = world.map(plugin.portalProvider()::getPortals)
                .orElseGet(plugin.portalProvider()::getPortals)
                .map(portal -> plugin.bundle().component("portal.list.entry", sender,
                        Placeholder.parsed("portal", portal.getName())))
                .toList();
        var message = world.map(ignored -> portals.isEmpty() ? "portal.list.empty.world" : "portal.list.world")
                .orElseGet(() -> portals.isEmpty() ? "portal.list.empty" : "portal.list");
        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("world", world.map(World::getName).orElse("")),
                Formatter.number("count", portals.size()),
                Formatter.joining("portals", portals));
        return SINGLE_SUCCESS;
    }
}
