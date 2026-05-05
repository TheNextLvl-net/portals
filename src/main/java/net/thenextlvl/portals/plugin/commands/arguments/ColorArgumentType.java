package net.thenextlvl.portals.plugin.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import com.mojang.brigadier.LiteralMessage;
import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ColorArgumentType implements CustomArgumentType.Converted<Color, String> {
    private static final SimpleCommandExceptionType INVALID_COLOR = new SimpleCommandExceptionType(
            new LiteralMessage("Expected color in #RRGGBB format")
    );

    @Override
    public Color convert(final String nativeType) throws CommandSyntaxException {
        if (!nativeType.matches("#[0-9a-fA-F]{6}")) throw INVALID_COLOR.create();
        final var rgb = Integer.parseInt(nativeType.substring(1), 16);
        return Color.fromRGB(rgb);
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public Color parse(final StringReader reader) throws CommandSyntaxException {
        try {
            return CustomArgumentType.Converted.super.parse(reader);
        } catch (final CommandSyntaxException e) {
            throw INVALID_COLOR.createWithContext(reader);
        }
    }
}
