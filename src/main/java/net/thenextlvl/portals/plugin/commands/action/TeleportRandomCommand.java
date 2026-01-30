package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.bounds.Bounds;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TeleportRandomCommand extends ActionCommand<Bounds> {
    private TeleportRandomCommand(final PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().teleportRandom(), "teleport-random");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new TeleportRandomCommand(plugin);
        return command.create().then(Commands.argument("world", ArgumentTypes.world())
                .then(command.boundsArgument())
                .then(command.radiusArgument())
                .executes(command));
    }

    private ArgumentBuilder<CommandSourceStack, ?> radiusArgument() {
        final var center = Commands.argument("center", ArgumentTypes.blockPosition());
        final var radius = Commands.argument("radius", IntegerArgumentType.integer(1));
        final var height = Commands.argument("height", IntegerArgumentType.integer(1));
        return center.then(radius.then(height.executes(this)));
    }

    private ArgumentBuilder<CommandSourceStack, ?> boundsArgument() {
        final var from = Commands.argument("from", ArgumentTypes.blockPosition());
        final var to = Commands.argument("to", ArgumentTypes.blockPosition());
        return from.then(to.executes(this));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var world = context.getArgument("world", World.class);

        var bounds = resolveArgument(context, "center", BlockPositionResolver.class).map(center -> {
            final var radius = context.getArgument("radius", int.class);
            final var height = context.getArgument("height", int.class);
            return Bounds.factory().radius(world, center, radius, height);
        }).orElse(null);

        if (bounds == null) {
            final var from = resolveArgument(context, "from", BlockPositionResolver.class).orElse(null);
            final var to = resolveArgument(context, "to", BlockPositionResolver.class).orElse(null);
            if (from != null && to != null) bounds = Bounds.factory().of(world, from, to);
        }

        if (bounds == null) bounds = Bounds.factory().of(world,
                -30_000_000, world.getMinHeight(), -30_000_000,
                30_000_000, world.getLogicalHeight(), 30_000_000
        );

        return addAction(context, bounds);
    }

    @Override
    protected void onSuccess(final CommandContext<CommandSourceStack> context, final Portal portal, final Bounds input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.teleport-random",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("world", input.world().map(World::getName).orElse(input.worldKey().asString())),
                Formatter.number("min_x", input.minX()),
                Formatter.number("min_y", input.minY()),
                Formatter.number("min_z", input.minZ()),
                Formatter.number("max_x", input.maxX()),
                Formatter.number("max_y", input.maxY()),
                Formatter.number("max_z", input.maxZ()));
    }
}
