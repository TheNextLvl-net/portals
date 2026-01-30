package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.Position;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.BrigadierCommand;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PortalCreateCommand extends SimpleCommand {
    public PortalCreateCommand(final PortalsPlugin plugin) {
        super(plugin, "create", "portals.command.create");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalCreateCommand(plugin);
        final var name = Commands.argument("name", StringArgumentType.string());
        return command.create().then(name
                .then(boundsArgument(command))
                .executes(command));
    }

    static RequiredArgumentBuilder<CommandSourceStack, BlockPositionResolver> boundsArgument(final Command<CommandSourceStack> command) {
        final var world = Commands.argument("world", ArgumentTypes.world());
        final var from = Commands.argument("from", ArgumentTypes.blockPosition());
        final var to = Commands.argument("to", ArgumentTypes.blockPosition());
        return from.then(to.executes(command).then(world.executes(command)));
    }

    static @Nullable BoundingBox getBounds(final BrigadierCommand command, final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var fromResolver = command.tryGetArgument(context, "from", BlockPositionResolver.class).orElse(null);
        final var toResolver = command.tryGetArgument(context, "to", BlockPositionResolver.class).orElse(null);

        final var from = fromResolver != null ? fromResolver.resolve(context.getSource()) : null;
        final var to = toResolver != null ? toResolver.resolve(context.getSource()) : null;

        final var world = command.tryGetArgument(context, "world", World.class).orElse(context.getSource().getLocation().getWorld());

        if (from != null && to != null) return BoundingBox.of(world, min(from, to), max(from, to).offset(1, 1, 1));

        if (!(context.getSource().getExecutor() instanceof final Player player)) return null;

        final var provider = command.plugin.getServer().getServicesManager().load(SelectionProvider.class);
        return provider != null ? provider.getSelection(player).orElse(null) : null;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var name = context.getArgument("name", String.class);

        if (plugin.portalProvider().hasPortal(name)) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "portal.create.already_exists",
                    Placeholder.parsed("portal", name));
            return 0;
        }

        final var boundingBox = getBounds(this, context);

        if (boundingBox == null) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "portal.selection");
            return 0;
        }

        final var portal = plugin.portalProvider().createPortal(name, boundingBox);
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.create.success",
                Placeholder.parsed("portal", portal.getName()));

        return SINGLE_SUCCESS;
    }

    private static Position min(final Position pos1, final Position pos2) {
        return Position.fine(Math.min(pos1.x(), pos2.x()), Math.min(pos1.y(), pos2.y()), Math.min(pos1.z(), pos2.z()));
    }

    private static Position max(final Position pos1, final Position pos2) {
        return Position.fine(Math.max(pos1.x(), pos2.x()), Math.max(pos1.y(), pos2.y()), Math.max(pos1.z(), pos2.z()));
    }
}
