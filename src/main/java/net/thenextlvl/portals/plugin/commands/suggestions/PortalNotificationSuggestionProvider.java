package net.thenextlvl.portals.plugin.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.notification.Notification;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.notification.NotificationType;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class PortalNotificationSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        final var portal = context.getLastChild().getArgument("portal", Portal.class);
        final var trigger = context.getLastChild().getArgument("trigger", NotificationTrigger.class);
        portal.getNotifications().findByTrigger(trigger)
                .map(Notification::type)
                .map(NotificationType::getName)
                .filter(string -> string.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
