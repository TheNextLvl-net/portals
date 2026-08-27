package net.thenextlvl.portals.plugin.effects;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.effect.PortalEffectType;
import net.thenextlvl.portals.effects.WaveEffect;
import net.thenextlvl.portals.plugin.commands.arguments.ColorArgumentType;
import net.thenextlvl.portals.plugin.commands.arguments.EnumArgumentType;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
        options.put("particle", ArgumentTypes.resource(RegistryKey.PARTICLE_TYPE));
        options.put("color", new ColorArgumentType());
        options.put("speed", DoubleArgumentType.doubleArg());
        options.put("particle-count", IntegerArgumentType.integer(1));
        return options;
    }

    @Override
    public WaveEffect create(final CommandInput options) {
        final var builder = builder().particle(Particle.DUST).color(Color.WHITE);

        options.get("amplitude", Double.class).ifPresent(builder::amplitude);
        options.get("wavelength", Double.class).ifPresent(builder::wavelength);
        options.get("wave-speed", Double.class).ifPresent(builder::waveSpeed);
        options.get("wave-type", WaveEffect.WaveType.class).ifPresent(builder::waveType);
        options.get("horizontal", Boolean.class).ifPresent(builder::horizontal);
        options.get("length", Double.class).ifPresent(builder::length);
        options.get("particle", Particle.class).ifPresent(builder::particle);
        options.get("color", Color.class).ifPresent(builder::color);
        options.get("speed", Double.class).ifPresent(builder::speed);
        options.get("particle-count", Integer.class).ifPresent(builder::particleCount);

        return builder.build();
    }

    @Override
    public WaveEffect deserialize(final CompoundTag tag, final TagDeserializationContext context) throws ParserException {
        final var builder = builder()
                .particle(Particle.valueOf(tag.get("particle").getAsString()))
                .speed(tag.get("speed").getAsDouble())
                .amplitude(tag.get("amplitude").getAsDouble())
                .wavelength(tag.get("wavelength").getAsDouble())
                .waveSpeed(tag.get("waveSpeed").getAsDouble())
                .waveType(WaveEffect.WaveType.valueOf(tag.get("waveType").getAsString()))
                .length(tag.get("length").getAsDouble());

        tag.optional("particleCount").ifPresent(count -> builder.particleCount(count.getAsInt()));
        tag.optional("horizontal").ifPresent(horizontal -> builder.horizontal(horizontal.getAsByte() != 0));
        tag.optional("color").<CompoundTag>map(Tag::getAsCompound)
                .map(WavePortalEffectType::deserializeColor)
                .ifPresent(builder::color);

        return builder.build();
    }

    @Override
    public CompoundTag serialize(final WaveEffect effect, final TagSerializationContext context) throws ParserException {
        final var tag = CompoundTag.builder()
                .put("particle", effect.getParticle().name())
                .put("speed", effect.getSpeed())
                .put("amplitude", effect.getAmplitude())
                .put("wavelength", effect.getWavelength())
                .put("waveSpeed", effect.getWaveSpeed())
                .put("waveType", effect.getWaveType().name())
                .put("length", effect.getLength());
        effect.getParticleCount().ifPresent(count -> tag.put("particleCount", count));
        if (effect instanceof SimpleWaveEffect simple && simple.configuredHorizontal() != null) tag.put("horizontal", simple.configuredHorizontal());
        else if (!(effect instanceof SimpleWaveEffect)) tag.put("horizontal", effect.isHorizontal());
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
