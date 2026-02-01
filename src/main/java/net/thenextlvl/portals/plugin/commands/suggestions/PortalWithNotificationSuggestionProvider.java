package net.thenextlvl.portals.plugin.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalProvider;
import net.thenextlvl.portals.notification.NotificationTrigger;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class PortalWithNotificationSuggestionProvider<T> implements SuggestionProvider<T> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<T> context, final SuggestionsBuilder builder) {
        PortalProvider.provider().getPortals()
                .filter(portal -> !portal.getNotificationTriggers().isEmpty())
                .map(Portal::getName)
                .filter(name -> name.toLowerCase().contains(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
