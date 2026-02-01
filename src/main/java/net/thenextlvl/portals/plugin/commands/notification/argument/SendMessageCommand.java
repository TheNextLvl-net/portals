package net.thenextlvl.portals.plugin.commands.notification.argument;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.notification.NotificationTypes;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SendMessageCommand extends PortalStringNotificationCommand {
    private SendMessageCommand(final PortalsPlugin plugin) {
        super(plugin, NotificationTypes.types().message(), "message", "message");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        return new SendMessageCommand(plugin).create();
    }
}
