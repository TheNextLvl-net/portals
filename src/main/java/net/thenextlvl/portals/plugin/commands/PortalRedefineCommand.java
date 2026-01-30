package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;
import static net.thenextlvl.portals.plugin.commands.PortalCreateCommand.boundsArgument;
import static net.thenextlvl.portals.plugin.commands.PortalCreateCommand.getBounds;

@NullMarked
final class PortalRedefineCommand extends SimpleCommand {
    private PortalRedefineCommand(final PortalsPlugin plugin) {
        super(plugin, "redefine", "portals.command.redefine");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalRedefineCommand(plugin);
        return command.create().then(portalArgument(plugin)
                .then(boundsArgument(command))
                .executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var sender = context.getSource().getSender();

        final var boundingBox = getBounds(this, context);

        if (boundingBox == null) {
            plugin.bundle().sendMessage(sender, "portal.selection");
            return 0;
        }

        final var portal = context.getArgument("portal", Portal.class);
        final var success = portal.setBoundingBox(boundingBox);

        plugin.bundle().sendMessage(sender, success ? "portal.redefine.success" : "nothing.changed",
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}
