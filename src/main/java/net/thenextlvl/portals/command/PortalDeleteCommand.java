package net.thenextlvl.portals.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.command.PortalCommand.portalArgument;

@NullMarked
public final class PortalDeleteCommand extends SimpleCommand {
    public PortalDeleteCommand(PortalsPlugin plugin) {
        super(plugin, "delete", "portals.command.delete");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalDeleteCommand(plugin);
        return command.create().then(portalArgument(plugin)).executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var portal = context.getArgument("portal", Portal.class);
        var success = plugin.portalProvider().removePortal(portal);
        var message = success ? "portal.delete.success" : "portal.delete.failed";
        return success ? SINGLE_SUCCESS : 0;
    }
}
