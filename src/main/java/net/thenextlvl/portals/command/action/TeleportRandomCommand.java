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
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.model.Bounds;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TeleportRandomCommand extends ActionCommand<Bounds> {
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

    @Override
    protected void onSuccess(CommandContext<CommandSourceStack> context, Portal portal, Bounds input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.teleport-random",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("world", input.world().getName()),
                Formatter.number("min_x", input.minX()),
                Formatter.number("min_y", input.minY()),
                Formatter.number("min_z", input.minZ()),
                Formatter.number("max_x", input.maxX()),
                Formatter.number("max_y", input.maxY()),
                Formatter.number("max_z", input.maxZ()));
    }
}
