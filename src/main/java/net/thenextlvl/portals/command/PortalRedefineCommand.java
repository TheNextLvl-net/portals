package net.thenextlvl.portals.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.command.PortalCommand.portalArgument;
import static net.thenextlvl.portals.command.PortalCreateCommand.boundsArgument;
import static net.thenextlvl.portals.command.PortalCreateCommand.getBounds;

@NullMarked
final class PortalRedefineCommand extends SimpleCommand {
    private PortalRedefineCommand(PortalsPlugin plugin) {
        super(plugin, "redefine", "portals.command.redefine");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalRedefineCommand(plugin);
        return command.create().then(portalArgument(plugin)
                .then(boundsArgument(command))
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var sender = context.getSource().getSender();

        var boundingBox = getBounds(this, context);

        if (boundingBox == null) {
            plugin.bundle().sendMessage(sender, "portal.selection");
            return 0;
        }

        var portal = context.getArgument("portal", Portal.class);
        var success = portal.setBoundingBox(boundingBox);

        plugin.bundle().sendMessage(sender, success ? "portal.redefine.success" : "nothing.changed",
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}
