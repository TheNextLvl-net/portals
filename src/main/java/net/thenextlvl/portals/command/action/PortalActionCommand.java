package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.command.PortalCommand.portalArgument;

@NullMarked
public final class PortalActionCommand extends BrigadierCommand {
    private PortalActionCommand(PortalsPlugin plugin) {
        super(plugin, "action", "portals.command.action");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalActionCommand(plugin);
        return command.create().then(portalArgument(plugin)
                .then(ConnectCommand.create(plugin))
                .then(RemoveCommand.create(plugin))
                .then(RunConsoleCommand.create(plugin))
                .then(RunPlayerCommand.create(plugin))
                .then(TeleportCommand.create(plugin))
                .then(TeleportPortalCommand.create(plugin))
                .then(TeleportRandomCommand.create(plugin))
                .then(TransferCommand.create(plugin)));
    }
}
