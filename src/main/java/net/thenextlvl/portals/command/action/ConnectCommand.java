package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ConnectCommand extends StringActionCommand {
    private ConnectCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().connect(), "connect", "server");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        return new ConnectCommand(plugin).create();
    }
}
