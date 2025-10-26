package net.thenextlvl.portals.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalCreateCommand extends SimpleCommand {
    public PortalCreateCommand(PortalsPlugin plugin) {
        super(plugin, "create", "portals.command.create");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalCreateCommand(plugin);
        var name = Commands.argument("name", StringArgumentType.string());

        var world = Commands.argument("world", ArgumentTypes.world());

        var from = Commands.argument("from", ArgumentTypes.finePosition());
        var to = Commands.argument("to", ArgumentTypes.finePosition());
        var cuboid = from.then(to.executes(command).then(world.executes(command)));

        return command.create().then(name
                .then(cuboid)
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var name = context.getArgument("name", String.class);

        if (plugin.portalProvider().hasPortal(name)) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "portal.create.already_exists",
                    Placeholder.parsed("portal", name));
            return 0;
        }

        BoundingBox boundingBox = null;

        var fromResolver = tryGetArgument(context, "from", FinePositionResolver.class).orElse(null);
        var toResolver = tryGetArgument(context, "to", FinePositionResolver.class).orElse(null);

        var from = fromResolver != null ? fromResolver.resolve(context.getSource()) : null;
        var to = toResolver != null ? toResolver.resolve(context.getSource()) : null;

        var world = tryGetArgument(context, "world", World.class).orElse(context.getSource().getLocation().getWorld());

        if (from != null && to != null) boundingBox = BoundingBox.cuboid(world, from, to);

        if (boundingBox == null && context.getSource().getExecutor() instanceof Player player) {
            var provider = plugin.getServer().getServicesManager().load(SelectionProvider.class);
            if (provider != null) boundingBox = provider.getSelection(player).orElse(null);
        }

        if (boundingBox == null) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "portal.selection");
            return 0;
        }

        var portal = plugin.portalProvider().createPortal(name, boundingBox);
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.create.success",
                Placeholder.parsed("portal", portal.getName()));

        return SINGLE_SUCCESS;
    }
}
