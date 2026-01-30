package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import net.thenextlvl.portals.plugin.commands.arguments.PortalArgumentType;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TeleportPortalCommand extends ActionCommand<PortalLike> {
    private TeleportPortalCommand(final PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().teleportPortal(), "teleport-portal");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new TeleportPortalCommand(plugin);
        return command.create().then(Commands.argument("target", new PortalArgumentType(plugin)).executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var target = context.getArgument("target", Portal.class);
        return addAction(context, target);
    }

    @Override
    protected void onSuccess(final CommandContext<CommandSourceStack> context, final Portal portal, final PortalLike input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.teleport-portal",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("target", input.getName()));
    }
}
