package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.lazy.LazyLocation;
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
}
