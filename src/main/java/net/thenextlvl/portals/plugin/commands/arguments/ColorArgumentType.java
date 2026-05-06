package net.thenextlvl.portals.plugin.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class ColorArgumentType implements CustomArgumentType<Color, String> {
    @Override
    public Color parse(final StringReader reader) throws CommandSyntaxException {
        try {
            return toColor(ArgumentTypes.namedColor().parse(reader));
        } catch (final CommandSyntaxException e) {
            return toColor(ArgumentTypes.hexColor().parse(reader));
        }
    }

    private Color toColor(final TextColor color) {
        return Color.fromRGB(color.value());
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return ArgumentTypes.namedColor().listSuggestions(context, builder);
    }
}
