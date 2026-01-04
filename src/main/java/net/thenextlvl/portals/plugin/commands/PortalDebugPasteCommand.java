package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@NullMarked
final class PortalDebugPasteCommand extends SimpleCommand {
    private PortalDebugPasteCommand(PortalsPlugin plugin) {
        super(plugin, "debug-paste", "portals.command.debug-paste");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalDebugPasteCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();

        CompletableFuture.supplyAsync(plugin.debugger::buildPaste).thenAccept(debug -> {
            plugin.bundle().sendMessage(sender, "portal.debug-paste.uploading");
            plugin.debugger.uploadPaste(debug).thenAccept(url -> {
                plugin.bundle().sendMessage(sender, "portal.debug-paste.uploaded", Placeholder.parsed("url", url));
            }).exceptionally(throwable -> {
                if (sender instanceof Player) {
                    plugin.bundle().sendMessage(sender, "portal.debug-paste.failed.upload");
                    plugin.bundle().sendMessage(sender, "portal.debug-paste", Placeholder.parsed("debug", debug));
                }
                plugin.getComponentLogger().warn("Failed to upload debug", throwable);
                return null;
            });
        }).exceptionally(throwable -> {
            if (!(sender instanceof ConsoleCommandSender))
                plugin.bundle().sendMessage(sender, "portal.debug-paste.failed");
            plugin.getComponentLogger().warn("Failed to build debug", throwable);
            return null;
        }).orTimeout(1, TimeUnit.SECONDS);

        return SINGLE_SUCCESS;
    }
}
