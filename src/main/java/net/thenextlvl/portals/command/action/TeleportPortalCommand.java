package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.command.argument.PortalArgumentType;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TeleportPortalCommand extends ActionCommand<PortalLike> {
    private TeleportPortalCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().teleportPortal(), "teleport-portal");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new TeleportPortalCommand(plugin);
        return command.create().then(Commands.argument("target", new PortalArgumentType(plugin)).executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var target = context.getArgument("target", Portal.class);
        return addAction(context, target);
    }

    @Override
    protected void onSuccess(CommandContext<CommandSourceStack> context, Portal portal, PortalLike input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.teleport-portal",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("target", input.getName()));
    }
}
