package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.plugin.model.LazyLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TeleportCommand extends ActionCommand<Location> {
    private TeleportCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().teleport(), "teleport");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new TeleportCommand(plugin);
        var position = Commands.argument("position", ArgumentTypes.finePosition()).executes(command);
        var rotation = Commands.argument("rotation", ArgumentTypes.rotation()).executes(command);
        var world = Commands.argument("world", ArgumentTypes.world()).executes(command);
        return command.create().then(world.then(position.then(rotation)));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var world = context.getArgument("world", World.class);

        var location = resolveArgument(context, "position", FinePositionResolver.class)
                .orElseGet(world::getSpawnLocation)
                .toLocation(world);
        resolveArgument(context, "rotation", RotationResolver.class)
                .ifPresent(location::setRotation);

        return addAction(context, new LazyLocation(location));
    }

    @Override
    protected void onSuccess(CommandContext<CommandSourceStack> context, Portal portal, Location input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.teleport",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("world", input.getWorld().getName()),
                Formatter.number("x", input.getX()),
                Formatter.number("y", input.getY()),
                Formatter.number("z", input.getZ()));
    }
}
