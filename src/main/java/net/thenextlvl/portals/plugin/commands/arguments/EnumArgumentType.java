package net.thenextlvl.portals.plugin.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class EnumArgumentType<E extends Enum<E>> implements CustomArgumentType.Converted<E, String> {
    private final Class<E> enumClass;

    public EnumArgumentType(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E convert(final String nativeType) {
        final var normalized = nativeType.toUpperCase(Locale.ROOT).replaceAll("-", "_");
        return Enum.valueOf(enumClass, normalized);
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .map(String::toLowerCase)
                .map(string -> string.replace("_", "-"))
                .map(StringArgumentType::escapeIfRequired)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
