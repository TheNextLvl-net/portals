package net.thenextlvl.portals.plugin.effects;

import net.thenextlvl.portals.effect.SimplePortalEffect;
import net.thenextlvl.portals.effects.BeamEffect;
import net.thenextlvl.portals.effects.FountainEffect;
import net.thenextlvl.portals.effects.HelixEffect;
import net.thenextlvl.portals.effects.PulseEffect;
import net.thenextlvl.portals.effects.RingEffect;
import net.thenextlvl.portals.effects.SphereEffect;
import net.thenextlvl.portals.effects.SpiralEffect;
import net.thenextlvl.portals.effects.VortexEffect;
import net.thenextlvl.portals.effects.WaterfallEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class SimpleBeamEffect extends SimplePortalEffect implements BeamEffect {
    private final Location start, end;
    private final double thickness, animationSpeed;
    private final int density;
    private final boolean animate;

    private SimpleBeamEffect(final Builder builder) {
        super(builder);
        this.start = builder.start;
        this.end = builder.end;
        this.thickness = builder.thickness;
        this.density = builder.density;
        this.animate = builder.animate;
        this.animationSpeed = builder.animationSpeed;
    }

    @Override public Location getStart() { return start; }
    @Override public Location getEnd() { return end; }
    @Override public double getThickness() { return thickness; }
    @Override public int getDensity() { return density; }
    @Override public boolean isAnimate() { return animate; }
    @Override public double getAnimationSpeed() { return animationSpeed; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.beam(this, player, origin); }
    @Override public BeamEffect.Builder toBuilder() { return copyBase(new Builder()).start(start).end(end).thickness(thickness).density(density).animate(animate).animationSpeed(animationSpeed); }

    static final class Builder extends SimplePortalEffect.Builder<BeamEffect, BeamEffect.Builder> implements BeamEffect.Builder {
        private Location start = new Location(null, 0, 0, 0), end = new Location(null, 0, 1, 0);
        private double thickness = 0, animationSpeed = 1;
        private int density = 20;
        private boolean animate = false;
        @Override public BeamEffect.Builder start(final Location location) { this.start = location; return this; }
        @Override public BeamEffect.Builder end(final Location location) { this.end = location; return this; }
        @Override public BeamEffect.Builder thickness(final double thickness) { this.thickness = thickness; return this; }
        @Override public BeamEffect.Builder density(final int density) { this.density = density; return this; }
        @Override public BeamEffect.Builder animate(final boolean animate) { this.animate = animate; return this; }
        @Override public BeamEffect.Builder animationSpeed(final double speed) { this.animationSpeed = speed; return this; }
        @Override public BeamEffect build() { return new SimpleBeamEffect(this); }
    }
}

@NullMarked
final class SimpleRingEffect extends SimplePortalEffect implements RingEffect {
    private final double radius, rotationSpeed, thickness, pulseSpeed;
    private final boolean horizontal, pulse;
    private SimpleRingEffect(final Builder builder) { super(builder); radius = builder.radius; horizontal = builder.horizontal; rotationSpeed = builder.rotationSpeed; thickness = builder.thickness; pulse = builder.pulse; pulseSpeed = builder.pulseSpeed; }
    @Override public double getRadius() { return radius; }
    @Override public boolean isHorizontal() { return horizontal; }
    @Override public double getRotationSpeed() { return rotationSpeed; }
    @Override public double getThickness() { return thickness; }
    @Override public boolean isPulse() { return pulse; }
    @Override public double getPulseSpeed() { return pulseSpeed; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.ring(this, player, origin); }
    @Override public RingEffect.Builder toBuilder() { return copyBase(new Builder()).radius(radius).horizontal(horizontal).rotationSpeed(rotationSpeed).thickness(thickness).pulse(pulse).pulseSpeed(pulseSpeed); }
    static final class Builder extends SimplePortalEffect.Builder<RingEffect, RingEffect.Builder> implements RingEffect.Builder {
        private double radius = 1, rotationSpeed = 0, thickness = 0, pulseSpeed = 1; private boolean horizontal = true, pulse = false;
        @Override public RingEffect.Builder radius(final double radius) { this.radius = radius; return this; }
        @Override public RingEffect.Builder horizontal(final boolean horizontal) { this.horizontal = horizontal; return this; }
        @Override public RingEffect.Builder rotationSpeed(final double speed) { this.rotationSpeed = speed; return this; }
        @Override public RingEffect.Builder thickness(final double thickness) { this.thickness = thickness; return this; }
        @Override public RingEffect.Builder pulse(final boolean pulse) { this.pulse = pulse; return this; }
        @Override public RingEffect.Builder pulseSpeed(final double speed) { this.pulseSpeed = speed; return this; }
        @Override public RingEffect build() { return new SimpleRingEffect(this); }
    }
}

@NullMarked
final class SimpleSphereEffect extends SimplePortalEffect implements SphereEffect {
    private final double radius, rotationSpeed; private final boolean filled, rotate, pulse; private final int density;
    private SimpleSphereEffect(final Builder builder) { super(builder); radius = builder.radius; filled = builder.filled; density = builder.density; rotate = builder.rotate; rotationSpeed = builder.rotationSpeed; pulse = builder.pulse; }
    @Override public double getRadius() { return radius; }
    @Override public boolean isFilled() { return filled; }
    @Override public int getDensity() { return density; }
    @Override public boolean isRotate() { return rotate; }
    @Override public double getRotationSpeed() { return rotationSpeed; }
    @Override public boolean isPulse() { return pulse; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.sphere(this, player, origin); }
    @Override public SphereEffect.Builder toBuilder() { return copyBase(new Builder()).radius(radius).filled(filled).density(density).rotate(rotate).rotationSpeed(rotationSpeed).pulse(pulse); }
    static final class Builder extends SimplePortalEffect.Builder<SphereEffect, SphereEffect.Builder> implements SphereEffect.Builder {
        private double radius = 1, rotationSpeed = 0; private boolean filled = false, rotate = false, pulse = false; private int density = 20;
        @Override public SphereEffect.Builder radius(final double radius) { this.radius = radius; return this; }
        @Override public SphereEffect.Builder filled(final boolean filled) { this.filled = filled; return this; }
        @Override public SphereEffect.Builder density(final int density) { this.density = density; return this; }
        @Override public SphereEffect.Builder rotate(final boolean rotate) { this.rotate = rotate; return this; }
        @Override public SphereEffect.Builder rotationSpeed(final double speed) { this.rotationSpeed = speed; return this; }
        @Override public SphereEffect.Builder pulse(final boolean pulse) { this.pulse = pulse; return this; }
        @Override public SphereEffect build() { return new SimpleSphereEffect(this); }
    }
}

@NullMarked
final class SimpleSpiralEffect extends SimplePortalEffect implements SpiralEffect {
    private final double radius, height, rotations, rotationSpeed; private final boolean ascending; private final int density;
    private SimpleSpiralEffect(final Builder builder) { super(builder); radius = builder.radius; height = builder.height; rotations = builder.rotations; ascending = builder.ascending; density = builder.density; rotationSpeed = builder.rotationSpeed; }
    @Override public double getRadius() { return radius; }
    @Override public double getHeight() { return height; }
    @Override public double getRotations() { return rotations; }
    @Override public boolean isAscending() { return ascending; }
    @Override public int getDensity() { return density; }
    @Override public double getRotationSpeed() { return rotationSpeed; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.spiral(this, player, origin); }
    @Override public SpiralEffect.Builder toBuilder() { return copyBase(new Builder()).radius(radius).height(height).rotations(rotations).ascending(ascending).density(density).rotationSpeed(rotationSpeed); }
    static final class Builder extends SimplePortalEffect.Builder<SpiralEffect, SpiralEffect.Builder> implements SpiralEffect.Builder {
        private double radius = 1, height = 2, rotations = 2, rotationSpeed = 0; private boolean ascending = true; private int density = 20;
        @Override public SpiralEffect.Builder radius(final double radius) { this.radius = radius; return this; }
        @Override public SpiralEffect.Builder height(final double height) { this.height = height; return this; }
        @Override public SpiralEffect.Builder rotations(final double rotations) { this.rotations = rotations; return this; }
        @Override public SpiralEffect.Builder ascending(final boolean ascending) { this.ascending = ascending; return this; }
        @Override public SpiralEffect.Builder density(final int density) { this.density = density; return this; }
        @Override public SpiralEffect.Builder rotationSpeed(final double speed) { this.rotationSpeed = speed; return this; }
        @Override public SpiralEffect build() { return new SimpleSpiralEffect(this); }
    }
}

@NullMarked
final class SimpleHelixEffect extends SimplePortalEffect implements HelixEffect {
    private final double radius, height, rotationSpeed, pitch; private final int strands;
    private SimpleHelixEffect(final Builder builder) { super(builder); radius = builder.radius; height = builder.height; strands = builder.strands; rotationSpeed = builder.rotationSpeed; pitch = builder.pitch; }
    @Override public double getRadius() { return radius; }
    @Override public double getHeight() { return height; }
    @Override public int getStrands() { return strands; }
    @Override public double getRotationSpeed() { return rotationSpeed; }
    @Override public double getPitch() { return pitch; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.helix(this, player, origin); }
    @Override public HelixEffect.Builder toBuilder() { return copyBase(new Builder()).radius(radius).height(height).strands(strands).rotationSpeed(rotationSpeed).pitch(pitch); }
    static final class Builder extends SimplePortalEffect.Builder<HelixEffect, HelixEffect.Builder> implements HelixEffect.Builder {
        private double radius = 1, height = 2, rotationSpeed = 0, pitch = 1; private int strands = 2;
        @Override public HelixEffect.Builder radius(final double radius) { this.radius = radius; return this; }
        @Override public HelixEffect.Builder height(final double height) { this.height = height; return this; }
        @Override public HelixEffect.Builder strands(final int strands) { this.strands = strands; return this; }
        @Override public HelixEffect.Builder rotationSpeed(final double speed) { this.rotationSpeed = speed; return this; }
        @Override public HelixEffect.Builder pitch(final double pitch) { this.pitch = pitch; return this; }
        @Override public HelixEffect build() { return new SimpleHelixEffect(this); }
    }
}

@NullMarked
final class SimplePulseEffect extends SimplePortalEffect implements PulseEffect {
    private final double minRadius, maxRadius, pulseSpeed; private final PulseShape shape; private final boolean fade; private final int waves;
    private SimplePulseEffect(final Builder builder) { super(builder); minRadius = builder.minRadius; maxRadius = builder.maxRadius; pulseSpeed = builder.pulseSpeed; shape = builder.shape; fade = builder.fade; waves = builder.waves; }
    @Override public double getMinRadius() { return minRadius; }
    @Override public double getMaxRadius() { return maxRadius; }
    @Override public double getPulseSpeed() { return pulseSpeed; }
    @Override public PulseShape getShape() { return shape; }
    @Override public boolean isFade() { return fade; }
    @Override public int getWaves() { return waves; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.pulse(this, player, origin); }
    @Override public PulseEffect.Builder toBuilder() { return copyBase(new Builder()).minRadius(minRadius).maxRadius(maxRadius).pulseSpeed(pulseSpeed).shape(shape).fade(fade).waves(waves); }
    static final class Builder extends SimplePortalEffect.Builder<PulseEffect, PulseEffect.Builder> implements PulseEffect.Builder {
        private double minRadius = .5, maxRadius = 2, pulseSpeed = 1; private PulseShape shape = PulseShape.CIRCLE; private boolean fade = false; private int waves = 1;
        @Override public PulseEffect.Builder minRadius(final double radius) { this.minRadius = radius; return this; }
        @Override public PulseEffect.Builder maxRadius(final double radius) { this.maxRadius = radius; return this; }
        @Override public PulseEffect.Builder pulseSpeed(final double speed) { this.pulseSpeed = speed; return this; }
        @Override public PulseEffect.Builder shape(final PulseShape shape) { this.shape = shape; return this; }
        @Override public PulseEffect.Builder fade(final boolean fade) { this.fade = fade; return this; }
        @Override public PulseEffect.Builder waves(final int waves) { this.waves = waves; return this; }
        @Override public PulseEffect build() { return new SimplePulseEffect(this); }
    }
}

@NullMarked
final class SimpleFountainEffect extends SimplePortalEffect implements FountainEffect {
    private final double baseRadius, maxHeight, velocity, spreadAngle; private final int sprayRate; private final boolean arc;
    private SimpleFountainEffect(final Builder builder) { super(builder); baseRadius = builder.baseRadius; maxHeight = builder.maxHeight; sprayRate = builder.sprayRate; velocity = builder.velocity; spreadAngle = builder.spreadAngle; arc = builder.arc; }
    @Override public double getBaseRadius() { return baseRadius; }
    @Override public double getMaxHeight() { return maxHeight; }
    @Override public int getSprayRate() { return sprayRate; }
    @Override public double getVelocity() { return velocity; }
    @Override public double getSpreadAngle() { return spreadAngle; }
    @Override public boolean isArc() { return arc; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.fountain(this, player, origin); }
    @Override public FountainEffect.Builder toBuilder() { return copyBase(new Builder()).baseRadius(baseRadius).maxHeight(maxHeight).sprayRate(sprayRate).velocity(velocity).spreadAngle(spreadAngle).arc(arc); }
    static final class Builder extends SimplePortalEffect.Builder<FountainEffect, FountainEffect.Builder> implements FountainEffect.Builder {
        private double baseRadius = .5, maxHeight = 2, velocity = 1, spreadAngle = 30; private int sprayRate = 10; private boolean arc = true;
        @Override public FountainEffect.Builder baseRadius(final double radius) { this.baseRadius = radius; return this; }
        @Override public FountainEffect.Builder maxHeight(final double height) { this.maxHeight = height; return this; }
        @Override public FountainEffect.Builder sprayRate(final int rate) { this.sprayRate = rate; return this; }
        @Override public FountainEffect.Builder velocity(final double velocity) { this.velocity = velocity; return this; }
        @Override public FountainEffect.Builder spreadAngle(final double angle) { this.spreadAngle = angle; return this; }
        @Override public FountainEffect.Builder arc(final boolean arc) { this.arc = arc; return this; }
        @Override public FountainEffect build() { return new SimpleFountainEffect(this); }
    }
}

@NullMarked
final class SimpleVortexEffect extends SimplePortalEffect implements VortexEffect {
    private final double baseRadius, topRadius, height, rotationSpeed; private final int streams; private final boolean pullInward;
    private SimpleVortexEffect(final Builder builder) { super(builder); baseRadius = builder.baseRadius; topRadius = builder.topRadius; height = builder.height; rotationSpeed = builder.rotationSpeed; streams = builder.streams; pullInward = builder.pullInward; }
    @Override public double getBaseRadius() { return baseRadius; }
    @Override public double getTopRadius() { return topRadius; }
    @Override public double getHeight() { return height; }
    @Override public double getRotationSpeed() { return rotationSpeed; }
    @Override public int getStreams() { return streams; }
    @Override public boolean isPullInward() { return pullInward; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.vortex(this, player, origin); }
    @Override public VortexEffect.Builder toBuilder() { return copyBase(new Builder()).baseRadius(baseRadius).topRadius(topRadius).height(height).rotationSpeed(rotationSpeed).streams(streams).pullInward(pullInward); }
    static final class Builder extends SimplePortalEffect.Builder<VortexEffect, VortexEffect.Builder> implements VortexEffect.Builder {
        private double baseRadius = 1, topRadius = .2, height = 2, rotationSpeed = 0; private int streams = 3; private boolean pullInward = true;
        @Override public VortexEffect.Builder baseRadius(final double radius) { this.baseRadius = radius; return this; }
        @Override public VortexEffect.Builder topRadius(final double radius) { this.topRadius = radius; return this; }
        @Override public VortexEffect.Builder height(final double height) { this.height = height; return this; }
        @Override public VortexEffect.Builder rotationSpeed(final double speed) { this.rotationSpeed = speed; return this; }
        @Override public VortexEffect.Builder streams(final int streams) { this.streams = streams; return this; }
        @Override public VortexEffect.Builder pullInward(final boolean inward) { this.pullInward = inward; return this; }
        @Override public VortexEffect build() { return new SimpleVortexEffect(this); }
    }
}

@NullMarked
final class SimpleWaterfallEffect extends SimplePortalEffect implements WaterfallEffect {
    private final double width, height, splashRadius, turbulence, fallSpeed; private final int flowRate;
    private SimpleWaterfallEffect(final Builder builder) { super(builder); width = builder.width; height = builder.height; flowRate = builder.flowRate; splashRadius = builder.splashRadius; turbulence = builder.turbulence; fallSpeed = builder.fallSpeed; }
    @Override public double getWidth() { return width; }
    @Override public double getHeight() { return height; }
    @Override public int getFlowRate() { return flowRate; }
    @Override public double getSplashRadius() { return splashRadius; }
    @Override public double getTurbulence() { return turbulence; }
    @Override public double getFallSpeed() { return fallSpeed; }
    @Override public void play(final Player player, final Location origin) { EffectRenderer.waterfall(this, player, origin); }
    @Override public WaterfallEffect.Builder toBuilder() { return copyBase(new Builder()).width(width).height(height).flowRate(flowRate).splash(splashRadius).turbulence(turbulence).fallSpeed(fallSpeed); }
    static final class Builder extends SimplePortalEffect.Builder<WaterfallEffect, WaterfallEffect.Builder> implements WaterfallEffect.Builder {
        private double width = 2, height = 3, splashRadius = .5, turbulence = .2, fallSpeed = 1; private int flowRate = 20;
        @Override public WaterfallEffect.Builder width(final double width) { this.width = width; return this; }
        @Override public WaterfallEffect.Builder height(final double height) { this.height = height; return this; }
        @Override public WaterfallEffect.Builder flowRate(final int rate) { this.flowRate = rate; return this; }
        @Override public WaterfallEffect.Builder splash(final double radius) { this.splashRadius = radius; return this; }
        @Override public WaterfallEffect.Builder turbulence(final double turbulence) { this.turbulence = turbulence; return this; }
        @Override public WaterfallEffect.Builder fallSpeed(final double speed) { this.fallSpeed = speed; return this; }
        @Override public WaterfallEffect build() { return new SimpleWaterfallEffect(this); }
    }
}
