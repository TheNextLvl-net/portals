package net.thenextlvl.portals.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.argument.PortalArgumentType;
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
                .then(PortalCooldownCommand.create(plugin))
                .then(PortalCostCommand.create(plugin))
                .then(PortalCreateCommand.create(plugin))
                .then(PortalDeleteCommand.create(plugin))
                .then(PortalListCommand.create(plugin))
                .then(PortalPermissionCommand.create(plugin))
                .then(PortalTeleportCommand.create(plugin))
                .build();
    }

    static RequiredArgumentBuilder<CommandSourceStack, Portal> portalArgument(PortalsPlugin plugin) {
        return Commands.argument("portal", new PortalArgumentType(plugin));
    }
}
