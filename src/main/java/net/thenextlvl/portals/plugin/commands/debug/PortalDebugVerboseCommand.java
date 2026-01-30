package net.thenextlvl.portals.plugin.commands.debug;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PortalDebugVerboseCommand extends SimpleCommand {
    private PortalDebugVerboseCommand(final PortalsPlugin plugin) {
        super(plugin, "verbose", "portals.command.debug.verbose");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalDebugVerboseCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var enabled = !plugin.debugger.getBroadcast();
        plugin.debugger.setBroadcast(enabled);
        plugin.bundle().sendMessage(sender, enabled
                ? "portal.debug.verbose.enabled"
                : "portal.debug.verbose.disabled");
        return SINGLE_SUCCESS;
    }
}
