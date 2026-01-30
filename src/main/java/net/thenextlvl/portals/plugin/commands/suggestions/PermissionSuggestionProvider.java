package net.thenextlvl.portals.plugin.commands.suggestions;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class PermissionSuggestionProvider<T> implements SuggestionProvider<T> {
    private final PortalsPlugin plugin;

    public PermissionSuggestionProvider(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<T> context, final SuggestionsBuilder builder) {
        plugin.getServer().getPluginManager().getPermissions().stream()
                .map(Permission::getName)
                .map(StringArgumentType::escapeIfRequired)
                .filter(string -> string.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
