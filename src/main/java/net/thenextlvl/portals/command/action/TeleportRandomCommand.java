package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.action.teleport.Bounds;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeleportRandomCommand extends ActionCommand<Bounds> {
    private TeleportRandomCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().teleportRandom(), "teleport-random");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new TeleportRandomCommand(plugin);
        return command.create().then(Commands.argument("world", ArgumentTypes.world())
                .then(command.boundsArgument())
                .then(command.radiusArgument()));
    }

    private ArgumentBuilder<CommandSourceStack, ?> radiusArgument() {
        var center = Commands.argument("center", ArgumentTypes.finePosition());
        var radius = Commands.argument("radius", DoubleArgumentType.doubleArg());
        var height = Commands.argument("height", DoubleArgumentType.doubleArg());
        return center.then(radius.then(height.executes(this)));
    }

    private ArgumentBuilder<CommandSourceStack, ?> boundsArgument() {
        var from = Commands.argument("from", ArgumentTypes.finePosition());
        var to = Commands.argument("to", ArgumentTypes.finePosition());
        return from.then(to.executes(this));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var world = context.getArgument("world", World.class);

        var bounds = resolveArgument(context, "center", FinePositionResolver.class).map(center -> {
            var radius = context.getArgument("radius", Double.class);
            var height = context.getArgument("height", Double.class);
            return Bounds.radius(world, center, radius, height);
        }).orElse(null);

        if (bounds == null) {
            var from = resolveArgument(context, "from", FinePositionResolver.class).orElseThrow();
            var to = resolveArgument(context, "to", FinePositionResolver.class).orElseThrow();
            bounds = new Bounds(world, from, to);
        }

        return addAction(context, bounds);
    }
}
