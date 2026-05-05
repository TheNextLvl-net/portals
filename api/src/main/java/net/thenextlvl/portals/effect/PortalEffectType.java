package net.thenextlvl.portals.effect;

import com.mojang.brigadier.arguments.ArgumentType;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface PortalEffectType<T extends PortalEffect, B extends PortalEffect.Builder<T, B>> {
    @Contract(pure = true)
    String getName();

    @Contract(pure = true)
    Class<T> getType();

    @Contract(pure = true)
    Class<B> getBuilderType();

    @Contract(value = " -> new", pure = true)
    B builder();

    @Contract(pure = true)
    Map<String, @Nullable ArgumentType<?>> getOptions();

    @Contract(pure = true)
    T create(@Nullable T current, CommandInput input);

    @Contract(pure = true)
    T deserialize(CompoundTag tag, TagDeserializationContext context) throws ParserException;

    @Contract(pure = true)
    CompoundTag serialize(T effect, TagSerializationContext context) throws ParserException;

    interface CommandInput {
        @Contract(pure = true)
        boolean has(String option);

        @Contract(pure = true)
        <V> Optional<V> get(String option, Class<V> type);
    }
}
