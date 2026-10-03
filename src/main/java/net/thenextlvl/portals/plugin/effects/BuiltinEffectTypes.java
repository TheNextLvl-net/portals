package net.thenextlvl.portals.plugin.effects;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectType;
import net.thenextlvl.portals.effects.FountainEffect;
import net.thenextlvl.portals.effects.HelixEffect;
import net.thenextlvl.portals.effects.PulseEffect;
import net.thenextlvl.portals.effects.RingEffect;
import net.thenextlvl.portals.effects.SphereEffect;
import net.thenextlvl.portals.effects.SpiralEffect;
import net.thenextlvl.portals.effects.VortexEffect;
import net.thenextlvl.portals.effects.WaterfallEffect;
import net.thenextlvl.portals.plugin.commands.arguments.ColorArgumentType;
import net.thenextlvl.portals.plugin.commands.arguments.EnumArgumentType;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BuiltinEffectTypes {
    private BuiltinEffectTypes() {
    }

    public static void register(final SimplePortalEffectTypeRegistry registry) {
        registry.register(new FountainPortalEffectType());
        registry.register(new HelixPortalEffectType());
        registry.register(new PulsePortalEffectType());
        registry.register(new RingPortalEffectType());
        registry.register(new SpherePortalEffectType());
        registry.register(new SpiralPortalEffectType());
        registry.register(new VortexPortalEffectType());
        registry.register(new WaterfallPortalEffectType());
        registry.register(new WavePortalEffectType());
    }
}

@NullMarked
abstract class BuiltinEffectType<T extends PortalEffect, B extends PortalEffect.Builder<T, B>> implements PortalEffectType<T, B> {
    @Override
    public Map<String, @Nullable ArgumentType<?>> getOptions() {
        final var options = new LinkedHashMap<String, @Nullable ArgumentType<?>>();
        options.put("particle", ArgumentTypes.resource(RegistryKey.PARTICLE_TYPE));
        options.put("color", new ColorArgumentType());
        options.put("speed", DoubleArgumentType.doubleArg());
        options.put("particle-count", IntegerArgumentType.integer(1));
        appendOptions(options);
        return options;
    }

    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> options) {
    }

    protected B defaultBuilder() {
        return builder().particle(Particle.DUST).color(Color.WHITE);
    }

    protected B applyBase(final B builder, final CommandInput input) {
        input.get("particle", Particle.class).ifPresent(builder::particle);
        input.get("color", Color.class).ifPresent(builder::color);
        input.get("speed", Double.class).ifPresent(builder::speed);
        input.get("particle-count", Integer.class).ifPresent(builder::particleCount);
        return builder;
    }

    protected B deserializeBase(final B builder, final CompoundTag tag, final TagDeserializationContext context) {
        builder.particle(Particle.valueOf(tag.get("particle").getAsString()))
                .speed(tag.get("speed").getAsDouble());
        tag.optional("particleCount").ifPresent(count -> builder.particleCount(count.getAsInt()));
        tag.optional("color").<CompoundTag>map(Tag::getAsCompound).map(BuiltinEffectType::deserializeColor).ifPresent(builder::color);
        return builder;
    }

    protected CompoundTag.Builder serializeBase(final T effect, final TagSerializationContext context) {
        final var tag = CompoundTag.builder()
                .put("particle", effect.getParticle().name())
                .put("speed", effect.getSpeed());
        effect.getParticleCount().ifPresent(count -> tag.put("particleCount", count));
        effect.getColor().ifPresent(color -> tag.put("color", serializeColor(color)));
        return tag;
    }

    private static CompoundTag serializeColor(final Color color) {
        return CompoundTag.builder().put("red", color.getRed()).put("green", color.getGreen()).put("blue", color.getBlue()).build();
    }

    private static Color deserializeColor(final CompoundTag tag) {
        return Color.fromRGB(tag.get("red").getAsInt(), tag.get("green").getAsInt(), tag.get("blue").getAsInt());
    }
}

@NullMarked
final class RingPortalEffectType extends BuiltinEffectType<RingEffect, RingEffect.Builder> {
    @Override
    public String getName() {
        return "ring";
    }

    @Override
    public Class<RingEffect> getType() {
        return RingEffect.class;
    }

    @Override
    public Class<RingEffect.Builder> getBuilderType() {
        return RingEffect.Builder.class;
    }

    @Override
    public RingEffect.Builder builder() {
        return new SimpleRingEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("radius", DoubleArgumentType.doubleArg(0));
        o.put("horizontal", BoolArgumentType.bool());
        o.put("rotation-speed", DoubleArgumentType.doubleArg());
        o.put("thickness", DoubleArgumentType.doubleArg(0));
        o.put("pulse", BoolArgumentType.bool());
        o.put("pulse-speed", DoubleArgumentType.doubleArg());
    }

    @Override
    public RingEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("radius", Double.class).ifPresent(b::radius);
        in.get("horizontal", Boolean.class).ifPresent(b::horizontal);
        in.get("rotation-speed", Double.class).ifPresent(b::rotationSpeed);
        in.get("thickness", Double.class).ifPresent(b::thickness);
        in.get("pulse", Boolean.class).ifPresent(b::pulse);
        in.get("pulse-speed", Double.class).ifPresent(b::pulseSpeed);
        return b.build();
    }

    @Override
    public RingEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).radius(t.get("radius").getAsDouble()).rotationSpeed(t.get("rotationSpeed").getAsDouble()).thickness(t.get("thickness").getAsDouble()).pulse(t.get("pulse").getAsByte() != 0).pulseSpeed(t.get("pulseSpeed").getAsDouble());
        t.optional("horizontal").ifPresent(tag -> b.horizontal(tag.getAsByte() != 0));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final RingEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("radius", e.getRadius()).put("rotationSpeed", e.getRotationSpeed()).put("thickness", e.getThickness()).put("pulse", e.isPulse()).put("pulseSpeed", e.getPulseSpeed());
        if (e instanceof SimpleRingEffect simple && simple.configuredHorizontal() != null) tag.put("horizontal", simple.configuredHorizontal());
        else if (!(e instanceof SimpleRingEffect)) tag.put("horizontal", e.isHorizontal());
        return tag.build();
    }
}

@NullMarked
final class SpherePortalEffectType extends BuiltinEffectType<SphereEffect, SphereEffect.Builder> {
    @Override
    public String getName() {
        return "sphere";
    }

    @Override
    public Class<SphereEffect> getType() {
        return SphereEffect.class;
    }

    @Override
    public Class<SphereEffect.Builder> getBuilderType() {
        return SphereEffect.Builder.class;
    }

    @Override
    public SphereEffect.Builder builder() {
        return new SimpleSphereEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("radius", DoubleArgumentType.doubleArg(0));
        o.put("density", IntegerArgumentType.integer(1));
        o.put("rotate", BoolArgumentType.bool());
        o.put("rotation-speed", DoubleArgumentType.doubleArg());
    }

    @Override
    public SphereEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("radius", Double.class).ifPresent(b::radius);
        in.get("density", Integer.class).ifPresent(b::density);
        in.get("rotate", Boolean.class).ifPresent(b::rotate);
        in.get("rotation-speed", Double.class).ifPresent(b::rotationSpeed);
        return b.build();
    }

    @Override
    public SphereEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).radius(t.get("radius").getAsDouble()).rotate(t.get("rotate").getAsByte() != 0).rotationSpeed(t.get("rotationSpeed").getAsDouble());
        t.optional("density").ifPresent(tag -> b.density(tag.getAsInt()));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final SphereEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("radius", e.getRadius()).put("rotate", e.isRotate()).put("rotationSpeed", e.getRotationSpeed());
        if (e.getDensity() > 0) tag.put("density", e.getDensity());
        return tag.build();
    }
}

@NullMarked
final class SpiralPortalEffectType extends BuiltinEffectType<SpiralEffect, SpiralEffect.Builder> {
    @Override
    public String getName() {
        return "spiral";
    }

    @Override
    public Class<SpiralEffect> getType() {
        return SpiralEffect.class;
    }

    @Override
    public Class<SpiralEffect.Builder> getBuilderType() {
        return SpiralEffect.Builder.class;
    }

    @Override
    public SpiralEffect.Builder builder() {
        return new SimpleSpiralEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("radius", DoubleArgumentType.doubleArg(0));
        o.put("height", DoubleArgumentType.doubleArg(0));
        o.put("rotations", DoubleArgumentType.doubleArg(0));
        o.put("spirals", IntegerArgumentType.integer(1));
        o.put("ascending", BoolArgumentType.bool());
        o.put("density", IntegerArgumentType.integer(1));
        o.put("rotation-speed", DoubleArgumentType.doubleArg());
    }

    @Override
    public SpiralEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("radius", Double.class).ifPresent(b::radius);
        in.get("height", Double.class).ifPresent(b::height);
        in.get("rotations", Double.class).ifPresent(b::rotations);
        in.get("spirals", Integer.class).ifPresent(b::spirals);
        in.get("ascending", Boolean.class).ifPresent(b::ascending);
        in.get("density", Integer.class).ifPresent(b::density);
        in.get("rotation-speed", Double.class).ifPresent(b::rotationSpeed);
        return b.build();
    }

    @Override
    public SpiralEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).rotations(t.get("rotations").getAsDouble()).ascending(t.get("ascending").getAsByte() != 0).density(t.get("density").getAsInt()).rotationSpeed(t.get("rotationSpeed").getAsDouble());
        t.optional("radius").ifPresent(tag -> b.radius(tag.getAsDouble()));
        t.optional("height").ifPresent(tag -> b.height(tag.getAsDouble()));
        t.optional("spirals").ifPresent(tag -> b.spirals(tag.getAsInt()));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final SpiralEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("rotations", e.getRotations()).put("spirals", e.getSpirals()).put("ascending", e.isAscending()).put("density", e.getDensity()).put("rotationSpeed", e.getRotationSpeed());
        e.getRadius().ifPresent(radius -> tag.put("radius", radius));
        e.getHeight().ifPresent(height -> tag.put("height", height));
        return tag.build();
    }
}

@NullMarked
final class HelixPortalEffectType extends BuiltinEffectType<HelixEffect, HelixEffect.Builder> {
    @Override
    public String getName() {
        return "helix";
    }

    @Override
    public Class<HelixEffect> getType() {
        return HelixEffect.class;
    }

    @Override
    public Class<HelixEffect.Builder> getBuilderType() {
        return HelixEffect.Builder.class;
    }

    @Override
    public HelixEffect.Builder builder() {
        return new SimpleHelixEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("radius", DoubleArgumentType.doubleArg(0));
        o.put("height", DoubleArgumentType.doubleArg(0));
        o.put("strands", IntegerArgumentType.integer(1));
        o.put("rotations", DoubleArgumentType.doubleArg(0));
    }

    @Override
    public HelixEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("radius", Double.class).ifPresent(b::radius);
        in.get("height", Double.class).ifPresent(b::height);
        in.get("strands", Integer.class).ifPresent(b::strands);
        in.get("rotations", Double.class).ifPresent(b::rotations);
        return b.build();
    }

    @Override
    public HelixEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).radius(t.get("radius").getAsDouble()).strands(t.get("strands").getAsInt());
        t.optional("rotations").or(() -> t.optional("pitch")).ifPresent(tag -> b.rotations(tag.getAsDouble()));
        t.optional("height").ifPresent(tag -> b.height(tag.getAsDouble()));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final HelixEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("radius", e.getRadius()).put("strands", e.getStrands()).put("rotations", e.getRotations());
        e.getHeight().ifPresent(height -> tag.put("height", height));
        return tag.build();
    }
}

@NullMarked
final class PulsePortalEffectType extends BuiltinEffectType<PulseEffect, PulseEffect.Builder> {
    @Override
    public String getName() {
        return "pulse";
    }

    @Override
    public Class<PulseEffect> getType() {
        return PulseEffect.class;
    }

    @Override
    public Class<PulseEffect.Builder> getBuilderType() {
        return PulseEffect.Builder.class;
    }

    @Override
    public PulseEffect.Builder builder() {
        return new SimplePulseEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("min-radius", DoubleArgumentType.doubleArg(0));
        o.put("max-radius", DoubleArgumentType.doubleArg(0));
        o.put("shape", new EnumArgumentType<>(PulseEffect.PulseShape.class));
        o.put("waves", IntegerArgumentType.integer(1));
    }

    @Override
    public PulseEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("min-radius", Double.class).ifPresent(b::minRadius);
        in.get("max-radius", Double.class).ifPresent(b::maxRadius);
        in.get("shape", PulseEffect.PulseShape.class).ifPresent(b::shape);
        in.get("waves", Integer.class).ifPresent(b::waves);
        return b.build();
    }

    @Override
    public PulseEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        return deserializeBase(builder(), t, c).minRadius(t.get("minRadius").getAsDouble()).maxRadius(t.get("maxRadius").getAsDouble()).shape(PulseEffect.PulseShape.valueOf(t.get("shape").getAsString())).waves(t.get("waves").getAsInt()).build();
    }

    @Override
    public CompoundTag serialize(final PulseEffect e, final TagSerializationContext c) {
        return serializeBase(e, c).put("minRadius", e.getMinRadius()).put("maxRadius", e.getMaxRadius()).put("shape", e.getShape().name()).put("waves", e.getWaves()).build();
    }
}

@NullMarked
final class FountainPortalEffectType extends BuiltinEffectType<FountainEffect, FountainEffect.Builder> {
    @Override
    public String getName() {
        return "fountain";
    }

    @Override
    public Class<FountainEffect> getType() {
        return FountainEffect.class;
    }

    @Override
    public Class<FountainEffect.Builder> getBuilderType() {
        return FountainEffect.Builder.class;
    }

    @Override
    public FountainEffect.Builder builder() {
        return new SimpleFountainEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("base-radius", DoubleArgumentType.doubleArg(0));
        o.put("max-height", DoubleArgumentType.doubleArg(0));
        o.put("spray-rate", IntegerArgumentType.integer(1));
    }

    @Override
    public FountainEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("base-radius", Double.class).ifPresent(b::baseRadius);
        in.get("max-height", Double.class).ifPresent(b::maxHeight);
        in.get("spray-rate", Integer.class).ifPresent(b::sprayRate);
        return b.build();
    }

    @Override
    public FountainEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).baseRadius(t.get("baseRadius").getAsDouble()).sprayRate(t.get("sprayRate").getAsInt());
        t.optional("maxHeight").ifPresent(tag -> b.maxHeight(tag.getAsDouble()));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final FountainEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("baseRadius", e.getBaseRadius()).put("sprayRate", e.getSprayRate());
        e.getMaxHeight().ifPresent(maxHeight -> tag.put("maxHeight", maxHeight));
        return tag.build();
    }
}

@NullMarked
final class VortexPortalEffectType extends BuiltinEffectType<VortexEffect, VortexEffect.Builder> {
    @Override
    public String getName() {
        return "vortex";
    }

    @Override
    public Class<VortexEffect> getType() {
        return VortexEffect.class;
    }

    @Override
    public Class<VortexEffect.Builder> getBuilderType() {
        return VortexEffect.Builder.class;
    }

    @Override
    public VortexEffect.Builder builder() {
        return new SimpleVortexEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("base-radius", DoubleArgumentType.doubleArg(0));
        o.put("top-radius", DoubleArgumentType.doubleArg(0));
        o.put("height", DoubleArgumentType.doubleArg(0));
        o.put("rotation-speed", DoubleArgumentType.doubleArg());
        o.put("streams", IntegerArgumentType.integer(1));
        o.put("pull-inward", BoolArgumentType.bool());
    }

    @Override
    public VortexEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("base-radius", Double.class).ifPresent(b::baseRadius);
        in.get("top-radius", Double.class).ifPresent(b::topRadius);
        in.get("height", Double.class).ifPresent(b::height);
        in.get("rotation-speed", Double.class).ifPresent(b::rotationSpeed);
        in.get("streams", Integer.class).ifPresent(b::streams);
        in.get("pull-inward", Boolean.class).ifPresent(b::pullInward);
        return b.build();
    }

    @Override
    public VortexEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).baseRadius(t.get("baseRadius").getAsDouble()).topRadius(t.get("topRadius").getAsDouble()).rotationSpeed(t.get("rotationSpeed").getAsDouble()).streams(t.get("streams").getAsInt()).pullInward(t.get("pullInward").getAsByte() != 0);
        t.optional("height").ifPresent(tag -> b.height(tag.getAsDouble()));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final VortexEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("baseRadius", e.getBaseRadius()).put("topRadius", e.getTopRadius()).put("rotationSpeed", e.getRotationSpeed()).put("streams", e.getStreams()).put("pullInward", e.isPullInward());
        e.getHeight().ifPresent(height -> tag.put("height", height));
        return tag.build();
    }
}

@NullMarked
final class WaterfallPortalEffectType extends BuiltinEffectType<WaterfallEffect, WaterfallEffect.Builder> {
    @Override
    public String getName() {
        return "waterfall";
    }

    @Override
    public Class<WaterfallEffect> getType() {
        return WaterfallEffect.class;
    }

    @Override
    public Class<WaterfallEffect.Builder> getBuilderType() {
        return WaterfallEffect.Builder.class;
    }

    @Override
    public WaterfallEffect.Builder builder() {
        return new SimpleWaterfallEffect.Builder();
    }

    @Override
    protected void appendOptions(final Map<String, @Nullable ArgumentType<?>> o) {
        o.put("width", DoubleArgumentType.doubleArg(0));
        o.put("height", DoubleArgumentType.doubleArg(0));
        o.put("flow-rate", IntegerArgumentType.integer(1));
        o.put("turbulence", DoubleArgumentType.doubleArg(0, 100));
        o.put("fall-speed", DoubleArgumentType.doubleArg());
    }

    @Override
    public WaterfallEffect create(final CommandInput in) {
        final var b = applyBase(defaultBuilder(), in);
        in.get("width", Double.class).ifPresent(b::width);
        in.get("height", Double.class).ifPresent(b::height);
        in.get("flow-rate", Integer.class).ifPresent(b::flowRate);
        in.get("turbulence", Double.class).map(turbulence -> turbulence / 100).ifPresent(b::turbulence);
        in.get("fall-speed", Double.class).ifPresent(b::fallSpeed);
        return b.build();
    }

    @Override
    public WaterfallEffect deserialize(final CompoundTag t, final TagDeserializationContext c) {
        final var b = deserializeBase(builder(), t, c).turbulence(t.get("turbulence").getAsDouble());
        t.optional("fallSpeed").ifPresent(tag -> b.fallSpeed(tag.getAsDouble()));
        t.optional("flowRate").ifPresent(tag -> b.flowRate(tag.getAsInt()));
        t.optional("width").ifPresent(tag -> b.width(tag.getAsDouble()));
        t.optional("height").ifPresent(tag -> b.height(tag.getAsDouble()));
        return b.build();
    }

    @Override
    public CompoundTag serialize(final WaterfallEffect e, final TagSerializationContext c) {
        final var tag = serializeBase(e, c).put("turbulence", e.getTurbulence());
        e.getFallSpeed().ifPresent(fallSpeed -> tag.put("fallSpeed", fallSpeed));
        e.getFlowRate().ifPresent(flowRate -> tag.put("flowRate", flowRate));
        e.getWidth().ifPresent(width -> tag.put("width", width));
        e.getHeight().ifPresent(height -> tag.put("height", height));
        return tag.build();
    }
}
