package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PortalTeleportCommand extends SimpleCommand {
    public PortalTeleportCommand(PortalsPlugin plugin) {
        super(plugin, "teleport", "portals.command.teleport");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalTeleportCommand(plugin);
        return command.create().then(PortalCommand.portalArgument(plugin).executes(command));
    }

    @Override
    protected boolean canUse(CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var portal = context.getArgument("portal", Portal.class);
        var player = (Player) context.getSource().getSender();

        player.teleportAsync(portal.getBoundingBox().getCenter()).thenAccept(success -> {
            var message = success ? "portal.teleport.success" : "portal.teleport.failed";
            plugin.bundle().sendMessage(player, message, Placeholder.parsed("portal", portal.getName()));
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Failed to teleport player to portal", throwable);
            plugin.bundle().sendMessage(player, "portal.teleport.failed", Placeholder.parsed("portal", portal.getName()));
            return null;
        });

        return SINGLE_SUCCESS;
    }
}