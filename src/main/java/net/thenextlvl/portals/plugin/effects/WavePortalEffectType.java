package net.thenextlvl.portals.plugin.effects;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.effect.PortalEffectType;
import net.thenextlvl.portals.effects.WaveEffect;
import net.thenextlvl.portals.plugin.commands.arguments.ColorArgumentType;
import net.thenextlvl.portals.plugin.commands.arguments.DurationArgumentType;
import net.thenextlvl.portals.plugin.commands.arguments.EnumArgumentType;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@NullMarked
public final class WavePortalEffectType implements PortalEffectType<WaveEffect, WaveEffect.Builder> {
    @Override
    public String getName() {
        return "wave";
    }

    @Override
    public Class<WaveEffect> getType() {
        return WaveEffect.class;
    }

    @Override
    public Class<WaveEffect.Builder> getBuilderType() {
        return WaveEffect.Builder.class;
    }

    @Override
    public WaveEffect.Builder builder() {
        return new SimpleWaveEffect.Builder();
    }

    @Override
    public Map<String, @Nullable ArgumentType<?>> getOptions() {
        final var options = new LinkedHashMap<String, @Nullable ArgumentType<?>>();
        options.put("amplitude", DoubleArgumentType.doubleArg(0));
        options.put("wavelength", DoubleArgumentType.doubleArg(Double.MIN_VALUE));
        options.put("wave-speed", DoubleArgumentType.doubleArg());
        options.put("wave-type", new EnumArgumentType<>(WaveEffect.WaveType.class));
        options.put("horizontal", BoolArgumentType.bool());
        options.put("length", DoubleArgumentType.doubleArg(Double.MIN_VALUE));
        options.put("particle", new EnumArgumentType<>(Particle.class));
        options.put("color", new ColorArgumentType());
        options.put("duration", DurationArgumentType.duration(Duration.ofMillis(50)));
        options.put("update-interval", DurationArgumentType.duration(Duration.ofMillis(50)));
        options.put("speed", DoubleArgumentType.doubleArg());
        options.put("particle-count", IntegerArgumentType.integer(1));
        return options;
    }

    @Override
    public WaveEffect create(final @Nullable WaveEffect current, final CommandInput options) {
        final var builder = current != null ? current.toBuilder() : builder().particle(Particle.PORTAL);

        options.get("amplitude", Double.class).ifPresent(builder::amplitude);
        options.get("wavelength", Double.class).ifPresent(builder::wavelength);
        options.get("wave-speed", Double.class).ifPresent(builder::waveSpeed);
        options.get("wave-type", WaveEffect.WaveType.class).ifPresent(builder::waveType);
        options.get("horizontal", Boolean.class).ifPresent(builder::horizontal);
        options.get("length", Double.class).ifPresent(builder::length);
        options.get("particle", Particle.class).ifPresent(builder::particle);
        options.get("color", Color.class).ifPresent(builder::color);
        options.get("duration", Duration.class).ifPresent(builder::duration);
        options.get("update-interval", Duration.class).ifPresent(builder::updateInterval);
        options.get("speed", Double.class).ifPresent(builder::speed);
        options.get("particle-count", Integer.class).ifPresent(builder::particleCount);

        return builder.build();
    }

    @Override
    public WaveEffect deserialize(final CompoundTag tag, final TagDeserializationContext context) throws ParserException {
        final var builder = builder()
                .particle(Particle.valueOf(tag.get("particle").getAsString()))
                .duration(context.deserialize(tag.get("duration"), Duration.class))
                .updateInterval(context.deserialize(tag.get("updateInterval"), Duration.class))
                .speed(tag.get("speed").getAsDouble())
                .particleCount(tag.get("particleCount").getAsInt())
                .amplitude(tag.get("amplitude").getAsDouble())
                .wavelength(tag.get("wavelength").getAsDouble())
                .waveSpeed(tag.get("waveSpeed").getAsDouble())
                .waveType(WaveEffect.WaveType.valueOf(tag.get("waveType").getAsString()))
                .horizontal(tag.get("horizontal").getAsByte() != 0)
                .length(tag.get("length").getAsDouble());

        tag.optional("color").<CompoundTag>map(Tag::getAsCompound)
                .map(WavePortalEffectType::deserializeColor)
                .ifPresent(builder::color);

        return builder.build();
    }

    @Override
    public CompoundTag serialize(final WaveEffect effect, final TagSerializationContext context) throws ParserException {
        final var tag = CompoundTag.builder()
                .put("particle", effect.getParticle().name())
                .put("duration", context.serialize(effect.getDuration()))
                .put("updateInterval", context.serialize(effect.getUpdateInterval()))
                .put("speed", effect.getSpeed())
                .put("particleCount", effect.getParticleCount())
                .put("amplitude", effect.getAmplitude())
                .put("wavelength", effect.getWavelength())
                .put("waveSpeed", effect.getWaveSpeed())
                .put("waveType", effect.getWaveType().name())
                .put("horizontal", effect.isHorizontal())
                .put("length", effect.getLength());
        effect.getColor().ifPresent(color -> tag.put("color", serializeColor(color)));
        return tag.build();
    }

    private static CompoundTag serializeColor(final Color color) {
        return CompoundTag.builder()
                .put("red", color.getRed())
                .put("green", color.getGreen())
                .put("blue", color.getBlue())
                .build();
    }

    private static Color deserializeColor(final CompoundTag tag) {
        return Color.fromRGB(tag.get("red").getAsInt(), tag.get("green").getAsInt(), tag.get("blue").getAsInt());
    }
}
