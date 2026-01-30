package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.action.PortalActionCommand;
import net.thenextlvl.portals.plugin.commands.arguments.PortalArgumentType;
import net.thenextlvl.portals.plugin.commands.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalCommand extends BrigadierCommand {
    public PortalCommand(final PortalsPlugin plugin) {
        super(plugin, "portal", "portals.command.portal");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalCommand(plugin);
        return command.create()
                .then(PortalActionCommand.create(plugin))
                .then(PortalCooldownCommand.create(plugin))
                .then(PortalCostCommand.create(plugin))
                .then(PortalCreateCommand.create(plugin))
                .then(PortalDebugPasteCommand.create(plugin))
                .then(PortalDeleteCommand.create(plugin))
                .then(PortalListCommand.create(plugin))
                .then(PortalPermissionCommand.create(plugin))
                .then(PortalRedefineCommand.create(plugin))
                .then(PortalTeleportCommand.create(plugin))
                .then(PortalWarmupCommand.create(plugin))
                .build();
    }

    public static RequiredArgumentBuilder<CommandSourceStack, Portal> portalArgument(final PortalsPlugin plugin) {
        return Commands.argument("portal", new PortalArgumentType(plugin));
    }
}
