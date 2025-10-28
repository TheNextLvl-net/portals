package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RunPlayerCommand extends StringActionCommand {
    private RunPlayerCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().runCommand(), "run-command", "command");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        return new RunPlayerCommand(plugin).create();
    }
}
