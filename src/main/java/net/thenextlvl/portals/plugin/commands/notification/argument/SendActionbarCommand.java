package net.thenextlvl.portals.plugin.commands.notification.argument;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SendActionbarCommand extends PortalStringNotificationCommand {
    private SendActionbarCommand(final PortalsPlugin plugin) {
        super(plugin, NotificationType.actionbar(), "message");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        return new SendActionbarCommand(plugin).create();
    }
}
