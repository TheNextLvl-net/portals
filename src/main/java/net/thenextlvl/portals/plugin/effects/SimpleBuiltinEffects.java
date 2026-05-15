package net.thenextlvl.portals.plugin.effects;

import net.thenextlvl.portals.effect.SimplePortalEffect;
import net.thenextlvl.portals.effects.FountainEffect;
import net.thenextlvl.portals.effects.HelixEffect;
import net.thenextlvl.portals.effects.PulseEffect;
import net.thenextlvl.portals.effects.RingEffect;
import net.thenextlvl.portals.effects.SphereEffect;
import net.thenextlvl.portals.effects.SpiralEffect;
import net.thenextlvl.portals.effects.VortexEffect;
import net.thenextlvl.portals.effects.WaterfallEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

@NullMarked
final class SimpleRingEffect extends SimplePortalEffect implements RingEffect {
    private final double radius, rotationSpeed, thickness, pulseSpeed;
    private final @Nullable Boolean horizontal;
    private final boolean pulse;

    private SimpleRingEffect(final Builder builder) {
        super(builder);
        radius = builder.radius;
        horizontal = builder.horizontal;
        rotationSpeed = builder.rotationSpeed;
        thickness = builder.thickness;
        pulse = builder.pulse;
        pulseSpeed = builder.pulseSpeed;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isHorizontal() {
        return horizontal != null ? horizontal : true;
    }

    boolean isHorizontal(final Location origin) {
        return horizontal != null ? horizontal : EffectRenderer.width(origin) >= EffectRenderer.height(origin);
    }

    @Nullable Boolean configuredHorizontal() {
        return horizontal;
    }

    @Override
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    @Override
    public double getThickness() {
        return thickness;
    }

    @Override
    public boolean isPulse() {
        return pulse;
    }

    @Override
    public double getPulseSpeed() {
        return pulseSpeed;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.ring(this, player, origin);
    }

    @Override
    public RingEffect.Builder toBuilder() {
        final var builder = copyBase(new Builder()).radius(radius).rotationSpeed(rotationSpeed).thickness(thickness).pulse(pulse).pulseSpeed(pulseSpeed);
        if (horizontal != null) builder.horizontal(horizontal);
        return builder;
    }

    static final class Builder extends SimplePortalEffect.Builder<RingEffect, RingEffect.Builder> implements RingEffect.Builder {
        private double radius = 1, rotationSpeed = 0, thickness = 0, pulseSpeed = 1;
        private @Nullable Boolean horizontal = null;
        private boolean pulse = false;

        @Override
        public RingEffect.Builder radius(final double radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public RingEffect.Builder horizontal(final boolean horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        @Override
        public RingEffect.Builder rotationSpeed(final double speed) {
            this.rotationSpeed = speed;
            return this;
        }

        @Override
        public RingEffect.Builder thickness(final double thickness) {
            this.thickness = thickness;
            return this;
        }

        @Override
        public RingEffect.Builder pulse(final boolean pulse) {
            this.pulse = pulse;
            return this;
        }

        @Override
        public RingEffect.Builder pulseSpeed(final double speed) {
            this.pulseSpeed = speed;
            return this;
        }

        @Override
        public RingEffect build() {
            return new SimpleRingEffect(this);
        }
    }
}

@NullMarked
final class SimpleSphereEffect extends SimplePortalEffect implements SphereEffect {
    private final double radius, rotationSpeed;
    private final boolean filled, rotate;
    private final int density;

    private SimpleSphereEffect(final Builder builder) {
        super(builder);
        radius = builder.radius;
        filled = builder.filled;
        density = builder.density;
        rotate = builder.rotate;
        rotationSpeed = builder.rotationSpeed;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isFilled() {
        return filled;
    }

    @Override
    public int getDensity() {
        return density;
    }

    @Override
    public boolean isRotate() {
        return rotate;
    }

    @Override
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.sphere(this, player, origin);
    }

    @Override
    public SphereEffect.Builder toBuilder() {
        return copyBase(new Builder()).radius(radius).filled(filled).density(density).rotate(rotate).rotationSpeed(rotationSpeed);
    }

    static final class Builder extends SimplePortalEffect.Builder<SphereEffect, SphereEffect.Builder> implements SphereEffect.Builder {
        private double radius = 1, rotationSpeed = 0;
        private boolean filled = false, rotate = false;
        private int density = 0;

        @Override
        public SphereEffect.Builder radius(final double radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public SphereEffect.Builder filled(final boolean filled) {
            this.filled = filled;
            return this;
        }

        @Override
        public SphereEffect.Builder density(final int density) {
            this.density = density;
            return this;
        }

        @Override
        public SphereEffect.Builder rotate(final boolean rotate) {
            this.rotate = rotate;
            return this;
        }

        @Override
        public SphereEffect.Builder rotationSpeed(final double speed) {
            this.rotationSpeed = speed;
            return this;
        }

        @Override
        public SphereEffect build() {
            return new SimpleSphereEffect(this);
        }
    }
}

@NullMarked
final class SimpleSpiralEffect extends SimplePortalEffect implements SpiralEffect {
    private final @Nullable Double radius, height;
    private final double rotations, rotationSpeed;
    private final boolean ascending;
    private final int density, spirals;

    private SimpleSpiralEffect(final Builder builder) {
        super(builder);
        radius = builder.radius;
        height = builder.height;
        rotations = builder.rotations;
        spirals = builder.spirals;
        ascending = builder.ascending;
        density = builder.density;
        rotationSpeed = builder.rotationSpeed;
    }

    @Override
    public Optional<Double> getRadius() {
        return Optional.ofNullable(radius);
    }

    @Override
    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public double getRotations() {
        return rotations;
    }

    @Override
    public int getSpirals() {
        return spirals;
    }

    @Override
    public boolean isAscending() {
        return ascending;
    }

    @Override
    public int getDensity() {
        return density;
    }

    @Override
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.spiral(this, player, origin);
    }

    @Override
    public SpiralEffect.Builder toBuilder() {
        return copyBase(new Builder()).radius(radius).height(height).rotations(rotations).spirals(spirals).ascending(ascending).density(density).rotationSpeed(rotationSpeed);
    }

    static final class Builder extends SimplePortalEffect.Builder<SpiralEffect, SpiralEffect.Builder> implements SpiralEffect.Builder {
        private @Nullable Double radius = null, height = null;
        private double rotations = 2.5, rotationSpeed = 8;
        private boolean ascending = false;
        private int density = 48, spirals = 1;

        Builder() {
            particleCount = 3;
            speed = 2;
        }

        @Override
        public SpiralEffect.Builder radius(final @Nullable Double radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public SpiralEffect.Builder height(final @Nullable Double height) {
            this.height = height;
            return this;
        }

        @Override
        public SpiralEffect.Builder rotations(final double rotations) {
            this.rotations = rotations;
            return this;
        }

        @Override
        public SpiralEffect.Builder spirals(final int spirals) {
            this.spirals = spirals;
            return this;
        }

        @Override
        public SpiralEffect.Builder ascending(final boolean ascending) {
            this.ascending = ascending;
            return this;
        }

        @Override
        public SpiralEffect.Builder density(final int density) {
            this.density = density;
            return this;
        }

        @Override
        public SpiralEffect.Builder rotationSpeed(final double speed) {
            this.rotationSpeed = speed;
            return this;
        }

        @Override
        public SpiralEffect build() {
            return new SimpleSpiralEffect(this);
        }
    }
}

@NullMarked
final class SimpleHelixEffect extends SimplePortalEffect implements HelixEffect {
    private final @Nullable Double height;
    private final double radius, rotations;
    private final int strands;

    private SimpleHelixEffect(final Builder builder) {
        super(builder);
        radius = builder.radius;
        height = builder.height;
        strands = builder.strands;
        rotations = builder.rotations;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public int getStrands() {
        return strands;
    }

    @Override
    public double getRotations() {
        return rotations;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.helix(this, player, origin);
    }

    @Override
    public HelixEffect.Builder toBuilder() {
        return copyBase(new Builder()).radius(radius).height(height).strands(strands).rotations(rotations);
    }

    static final class Builder extends SimplePortalEffect.Builder<HelixEffect, HelixEffect.Builder> implements HelixEffect.Builder {
        private @Nullable Double height = null;
        private double radius = 1, rotations = 1;
        private int strands = 2;

        @Override
        public HelixEffect.Builder radius(final double radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public HelixEffect.Builder height(final @Nullable Double height) {
            this.height = height;
            return this;
        }

        @Override
        public HelixEffect.Builder strands(final int strands) {
            this.strands = strands;
            return this;
        }

        @Override
        public HelixEffect.Builder rotations(final double rotations) {
            this.rotations = rotations;
            return this;
        }

        @Override
        public HelixEffect build() {
            return new SimpleHelixEffect(this);
        }
    }
}

@NullMarked
final class SimplePulseEffect extends SimplePortalEffect implements PulseEffect {
    private final double minRadius, maxRadius;
    private final PulseShape shape;
    private final int waves;

    private SimplePulseEffect(final Builder builder) {
        super(builder);
        minRadius = builder.minRadius;
        maxRadius = builder.maxRadius;
        shape = builder.shape;
        waves = builder.waves;
    }

    @Override
    public double getMinRadius() {
        return minRadius;
    }

    @Override
    public double getMaxRadius() {
        return maxRadius;
    }

    @Override
    public PulseShape getShape() {
        return shape;
    }

    @Override
    public int getWaves() {
        return waves;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.pulse(this, player, origin);
    }

    @Override
    public PulseEffect.Builder toBuilder() {
        return copyBase(new Builder()).minRadius(minRadius).maxRadius(maxRadius).shape(shape).waves(waves);
    }

    static final class Builder extends SimplePortalEffect.Builder<PulseEffect, PulseEffect.Builder> implements PulseEffect.Builder {
        private double minRadius = .5, maxRadius = 2;
        private PulseShape shape = PulseShape.CIRCLE;
        private int waves = 1;

        @Override
        public PulseEffect.Builder minRadius(final double radius) {
            this.minRadius = radius;
            return this;
        }

        @Override
        public PulseEffect.Builder maxRadius(final double radius) {
            this.maxRadius = radius;
            return this;
        }

        @Override
        public PulseEffect.Builder shape(final PulseShape shape) {
            this.shape = shape;
            return this;
        }

        @Override
        public PulseEffect.Builder waves(final int waves) {
            this.waves = waves;
            return this;
        }

        @Override
        public PulseEffect build() {
            return new SimplePulseEffect(this);
        }
    }
}

@NullMarked
final class SimpleFountainEffect extends SimplePortalEffect implements FountainEffect {
    private final @Nullable Double maxHeight;
    private final double baseRadius;
    private final int sprayRate;

    private SimpleFountainEffect(final Builder builder) {
        super(builder);
        baseRadius = builder.baseRadius;
        maxHeight = builder.maxHeight;
        sprayRate = builder.sprayRate;
    }

    @Override
    public double getBaseRadius() {
        return baseRadius;
    }

    @Override
    public Optional<Double> getMaxHeight() {
        return Optional.ofNullable(maxHeight);
    }

    @Override
    public int getSprayRate() {
        return sprayRate;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.fountain(this, player, origin);
    }

    @Override
    public FountainEffect.Builder toBuilder() {
        return copyBase(new Builder()).baseRadius(baseRadius).maxHeight(maxHeight).sprayRate(sprayRate);
    }

    static final class Builder extends SimplePortalEffect.Builder<FountainEffect, FountainEffect.Builder> implements FountainEffect.Builder {
        private @Nullable Double maxHeight = null;
        private double baseRadius = .5;
        private int sprayRate = 10;

        @Override
        public FountainEffect.Builder baseRadius(final double radius) {
            this.baseRadius = radius;
            return this;
        }

        @Override
        public FountainEffect.Builder maxHeight(final @Nullable Double height) {
            this.maxHeight = height;
            return this;
        }

        @Override
        public FountainEffect.Builder sprayRate(final int rate) {
            this.sprayRate = rate;
            return this;
        }

        @Override
        public FountainEffect build() {
            return new SimpleFountainEffect(this);
        }
    }
}

@NullMarked
final class SimpleVortexEffect extends SimplePortalEffect implements VortexEffect {
    private final @Nullable Double height;
    private final double baseRadius, topRadius, rotationSpeed;
    private final int streams;
    private final boolean pullInward;

    private SimpleVortexEffect(final Builder builder) {
        super(builder);
        baseRadius = builder.baseRadius;
        topRadius = builder.topRadius;
        height = builder.height;
        rotationSpeed = builder.rotationSpeed;
        streams = builder.streams;
        pullInward = builder.pullInward;
    }

    @Override
    public double getBaseRadius() {
        return baseRadius;
    }

    @Override
    public double getTopRadius() {
        return topRadius;
    }

    @Override
    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    @Override
    public int getStreams() {
        return streams;
    }

    @Override
    public boolean isPullInward() {
        return pullInward;
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.vortex(this, player, origin);
    }

    @Override
    public VortexEffect.Builder toBuilder() {
        return copyBase(new Builder()).baseRadius(baseRadius).topRadius(topRadius).height(height).rotationSpeed(rotationSpeed).streams(streams).pullInward(pullInward);
    }

    static final class Builder extends SimplePortalEffect.Builder<VortexEffect, VortexEffect.Builder> implements VortexEffect.Builder {
        private @Nullable Double height = null;
        private double baseRadius = 1, topRadius = .2, rotationSpeed = 0;
        private int streams = 3;
        private boolean pullInward = true;

        @Override
        public VortexEffect.Builder baseRadius(final double radius) {
            this.baseRadius = radius;
            return this;
        }

        @Override
        public VortexEffect.Builder topRadius(final double radius) {
            this.topRadius = radius;
            return this;
        }

        @Override
        public VortexEffect.Builder height(final @Nullable Double height) {
            this.height = height;
            return this;
        }

        @Override
        public VortexEffect.Builder rotationSpeed(final double speed) {
            this.rotationSpeed = speed;
            return this;
        }

        @Override
        public VortexEffect.Builder streams(final int streams) {
            this.streams = streams;
            return this;
        }

        @Override
        public VortexEffect.Builder pullInward(final boolean inward) {
            this.pullInward = inward;
            return this;
        }

        @Override
        public VortexEffect build() {
            return new SimpleVortexEffect(this);
        }
    }
}

@NullMarked
final class SimpleWaterfallEffect extends SimplePortalEffect implements WaterfallEffect {
    private final @Nullable Double width, height;
    private final @Nullable Integer flowRate;
    private final @Nullable Double fallSpeed;
    private final double turbulence;

    private SimpleWaterfallEffect(final Builder builder) {
        super(builder);
        width = builder.width;
        height = builder.height;
        flowRate = builder.flowRate;
        turbulence = builder.turbulence;
        fallSpeed = builder.fallSpeed;
    }

    @Override
    public Optional<Double> getWidth() {
        return Optional.ofNullable(width);
    }

    @Override
    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public OptionalInt getFlowRate() {
        return flowRate != null ? OptionalInt.of(flowRate) : OptionalInt.empty();
    }

    @Override
    public double getTurbulence() {
        return turbulence;
    }

    @Override
    public Optional<Double> getFallSpeed() {
        return Optional.ofNullable(fallSpeed);
    }

    @Override
    public void play(final Player player, final Location origin) {
        EffectRenderer.waterfall(this, player, origin);
    }

    @Override
    public WaterfallEffect.Builder toBuilder() {
        return copyBase(new Builder()).width(width).height(height).flowRate(flowRate).turbulence(turbulence).fallSpeed(fallSpeed);
    }

    static final class Builder extends SimplePortalEffect.Builder<WaterfallEffect, WaterfallEffect.Builder> implements WaterfallEffect.Builder {
        private @Nullable Double width = null, height = null;
        private @Nullable Integer flowRate = null;
        private @Nullable Double fallSpeed = null;
        private double turbulence = 0;

        @Override
        public WaterfallEffect.Builder width(final @Nullable Double width) {
            this.width = width;
            return this;
        }

        @Override
        public WaterfallEffect.Builder height(final @Nullable Double height) {
            this.height = height;
            return this;
        }

        @Override
        public WaterfallEffect.Builder flowRate(final @Nullable Integer rate) {
            this.flowRate = rate;
            return this;
        }

        @Override
        public WaterfallEffect.Builder turbulence(final double turbulence) {
            this.turbulence = turbulence;
            return this;
        }

        @Override
        public WaterfallEffect.Builder fallSpeed(final @Nullable Double speed) {
            this.fallSpeed = speed;
            return this;
        }

        @Override
        public WaterfallEffect build() {
            return new SimpleWaterfallEffect(this);
        }
    }
}
