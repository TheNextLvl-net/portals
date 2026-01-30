package net.thenextlvl.portals.plugin.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.util.Tick;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public final class DurationArgumentType implements CustomArgumentType.Converted<Duration, Integer> {
    private final Duration minimum;

    private DurationArgumentType(final Duration minimum) {
        this.minimum = minimum;
    }

    @Contract(value = " -> new", pure = true)
    public static DurationArgumentType duration() {
        return duration(Duration.ZERO);
    }

    @Contract(value = "_ -> new", pure = true)
    public static DurationArgumentType duration(final Duration minimum) {
        return new DurationArgumentType(minimum);
    }

    @Override
    public Duration convert(final Integer nativeType) {
        return Tick.of(nativeType);
    }

    @Override
    public ArgumentType<Integer> getNativeType() {
        return ArgumentTypes.time(Tick.tick().fromDuration(minimum));
    }
}
