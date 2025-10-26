package net.thenextlvl.portals.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalCreateCommand extends SimpleCommand {
    public PortalCreateCommand(PortalsPlugin plugin) {
        super(plugin, "create", "portals.command.create");
    }
    
    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalCreateCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return SINGLE_SUCCESS;
    }
}
