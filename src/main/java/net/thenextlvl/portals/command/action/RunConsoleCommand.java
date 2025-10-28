package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RunConsoleCommand extends StringActionCommand {
    private RunConsoleCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().runConsoleCommand(), "run-console-command", "command");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        return new RunConsoleCommand(plugin).create();
    }
}
