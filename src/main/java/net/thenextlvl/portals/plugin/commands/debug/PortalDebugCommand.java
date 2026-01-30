package net.thenextlvl.portals.plugin.commands.debug;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalDebugCommand extends BrigadierCommand {
    private PortalDebugCommand(final PortalsPlugin plugin) {
        super(plugin, "debug", "portals.command.debug");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        return new PortalDebugCommand(plugin).create()
                .then(PortalDebugPasteCommand.create(plugin))
                .then(PortalDebugVerboseCommand.create(plugin));
    }
}
