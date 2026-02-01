package net.thenextlvl.portals.plugin.commands.notification;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalNotificationCommand extends BrigadierCommand {
    private PortalNotificationCommand(final PortalsPlugin plugin) {
        super(plugin, "notification", "portals.command.notification");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalNotificationCommand(plugin);
        return command.create()
                .then(NotificationListCommand.create(plugin))
                .then(NotificationRemoveCommand.create(plugin))
                .then(NotificationSetCommand.create(plugin));
    }
}
