package net.thenextlvl.portals.command;

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
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.BrigadierCommand;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PortalCreateCommand extends SimpleCommand {
    public PortalCreateCommand(PortalsPlugin plugin) {
        super(plugin, "create", "portals.command.create");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalCreateCommand(plugin);
        var name = Commands.argument("name", StringArgumentType.string());
        return command.create().then(name
                .then(boundsArgument(command))
                .executes(command));
    }

    static RequiredArgumentBuilder<CommandSourceStack, BlockPositionResolver> boundsArgument(Command<CommandSourceStack> command) {
        var world = Commands.argument("world", ArgumentTypes.world());
        var from = Commands.argument("from", ArgumentTypes.blockPosition());
        var to = Commands.argument("to", ArgumentTypes.blockPosition());
        return from.then(to.executes(command).then(world.executes(command)));
    }

    static @Nullable BoundingBox getBounds(BrigadierCommand command, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var fromResolver = command.tryGetArgument(context, "from", BlockPositionResolver.class).orElse(null);
        var toResolver = command.tryGetArgument(context, "to", BlockPositionResolver.class).orElse(null);

        var from = fromResolver != null ? fromResolver.resolve(context.getSource()) : null;
        var to = toResolver != null ? toResolver.resolve(context.getSource()) : null;

        var world = command.tryGetArgument(context, "world", World.class).orElse(context.getSource().getLocation().getWorld());

        if (from != null && to != null) return BoundingBox.of(world, min(from, to), max(from, to).offset(1, 1, 1));

        if (!(context.getSource().getExecutor() instanceof Player player)) return null;

        var provider = command.plugin.getServer().getServicesManager().load(SelectionProvider.class);
        return provider != null ? provider.getSelection(player).orElse(null) : null;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var name = context.getArgument("name", String.class);

        if (plugin.portalProvider().hasPortal(name)) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "portal.create.already_exists",
                    Placeholder.parsed("portal", name));
            return 0;
        }

        var boundingBox = getBounds(this, context);

        if (boundingBox == null) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "portal.selection");
            return 0;
        }

        var portal = plugin.portalProvider().createPortal(name, boundingBox);
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.create.success",
                Placeholder.parsed("portal", portal.getName()));

        return SINGLE_SUCCESS;
    }

    private static Position min(Position pos1, Position pos2) {
        return Position.fine(Math.min(pos1.x(), pos2.x()), Math.min(pos1.y(), pos2.y()), Math.min(pos1.z(), pos2.z()));
    }

    private static Position max(Position pos1, Position pos2) {
        return Position.fine(Math.max(pos1.x(), pos2.x()), Math.max(pos1.y(), pos2.y()), Math.max(pos1.z(), pos2.z()));
    }
}
