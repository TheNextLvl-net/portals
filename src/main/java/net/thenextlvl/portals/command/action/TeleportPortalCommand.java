package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.command.argument.PortalArgumentType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeleportPortalCommand extends ActionCommand<Portal> {
    private TeleportPortalCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().teleportPortal(), "teleport-portal");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new TeleportPortalCommand(plugin);
        return command.create().then(Commands.argument("target", new PortalArgumentType(plugin)).executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var target = context.getArgument("target", Portal.class);
        return addAction(context, target);
    }
}
