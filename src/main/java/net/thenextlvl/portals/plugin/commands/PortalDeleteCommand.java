package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class PortalDeleteCommand extends SimpleCommand {
    public PortalDeleteCommand(final PortalsPlugin plugin) {
        super(plugin, "delete", "portals.command.delete");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalDeleteCommand(plugin);
        return command.create().then(portalArgument(plugin).executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var portal = context.getArgument("portal", Portal.class);
        final var success = plugin.portalProvider().deletePortal(portal);
        final var message = success ? "portal.delete.success" : "portal.delete.failed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}
