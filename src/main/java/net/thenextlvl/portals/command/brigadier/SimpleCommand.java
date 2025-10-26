package net.thenextlvl.portals.command.brigadier;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.PortalsPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class SimpleCommand extends BrigadierCommand implements Command<CommandSourceStack> {
    protected SimpleCommand(PortalsPlugin plugin, String name, String permission) {
        super(plugin, name, permission);
    }
}
