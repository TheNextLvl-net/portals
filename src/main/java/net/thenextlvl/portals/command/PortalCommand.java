package net.thenextlvl.portals.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalCommand extends BrigadierCommand {
    public PortalCommand(PortalsPlugin plugin) {
        super(plugin, "portal", "portals.command.portal");
    }

    public static LiteralCommandNode<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalCommand(plugin);
        return command.create()
                .then(PortalCreateCommand.create(plugin))
                .build();
    }
}
