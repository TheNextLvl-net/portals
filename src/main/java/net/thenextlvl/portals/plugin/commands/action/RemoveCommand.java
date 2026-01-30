package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class RemoveCommand extends SimpleCommand {
    private RemoveCommand(final PortalsPlugin plugin) {
        super(plugin, "remove", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new RemoveCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var portal = context.getArgument("portal", Portal.class);
        final var success = portal.setEntryAction(null);
        final var message = success ? "portal.action.removed" : "nothing.changed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}
