package net.thenextlvl.portals.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import core.paper.brigadier.exceptions.ComponentCommandExceptionType;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class PortalArgumentType implements CustomArgumentType.Converted<Portal, String> {
    private final PortalsPlugin plugin;

    public PortalArgumentType(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Portal convert(String nativeType) throws CommandSyntaxException {
        return plugin.portalProvider().getPortal(nativeType).orElseThrow(() -> new ComponentCommandExceptionType(
                Component.text("Unknown portal: " + nativeType)
        ).create());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        plugin.portalProvider().getPortals()
                .map(Portal::getName)
                .map(StringArgumentType::escapeIfRequired)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}
