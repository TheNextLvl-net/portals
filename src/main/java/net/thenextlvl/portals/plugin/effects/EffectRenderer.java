package net.thenextlvl.portals.plugin.effects;

import net.thenextlvl.portals.effect.PortalEffect;
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

import java.util.concurrent.ThreadLocalRandom;

@NullMarked
final class EffectRenderer {
    private EffectRenderer() {
    }

    static void ring(final RingEffect effect, final Player player, final Location origin) {
        final var count = particleCount(effect, origin, 12);
        final var tick = animationTick(origin);
        final var thickness = Math.max(0, effect.getThickness());
        final var pulse = effect.isPulse() ? Math.sin(tick * effect.getPulseSpeed()) * thickness : 0;
        final var radius = scaleHorizontal(origin, Math.max(0, effect.getRadius() + pulse));
        final var offset = Math.toRadians(tick * effect.getRotationSpeed());
        final var horizontal = effect instanceof SimpleRingEffect simple ? simple.isHorizontal(origin) : effect.isHorizontal();
        for (var i = 0; i < count; i++) {
            final var angle = Math.TAU * i / count + offset;
            final var band = thickness <= 0 ? 0 : ((i % 4) / 3d - .5) * thickness;
            final var sampleRadius = Math.max(0, radius + band);
            final var x = Math.cos(angle) * sampleRadius;
            final var y = horizontal ? 0 : Math.sin(angle) * sampleRadius;
            final var z = horizontal ? Math.sin(angle) * sampleRadius : 0;
            spawn(effect, player, point(origin, x, y, z));
        }
    }

    static void sphere(final SphereEffect effect, final Player player, final Location origin) {
        final var radius = Math.max(0, effect.getRadius());
        final var count = effect.getParticleCount().orElse(Math.max(8, effect.getDensity() > 0
                ? effect.getDensity()
                : (int) Math.ceil(radius * 1000)));
        final var offset = Math.toRadians(effect.isRotate() ? animationTick(origin) * effect.getRotationSpeed() : 0);
        for (var i = 0; i < count; i++) {
            final var phi = Math.acos(1 - 2 * (i + .5) / count);
            final var theta = Math.PI * (1 + Math.sqrt(5)) * i + offset;
            spawn(effect, player, point(origin,
                    Math.cos(theta) * Math.sin(phi) * radius,
                    Math.cos(phi) * radius,
                    Math.sin(theta) * Math.sin(phi) * radius
            ));
        }
    }

    static void spiral(final SpiralEffect effect, final Player player, final Location origin) {
        final var count = particleCount(effect, origin, 4);
        final var samples = Math.max(count, (int) Math.ceil(effect.getDensity() * Math.max(1, effect.getRotations())));
        final var radius = effect.getRadius().map(value -> scaleHorizontal(origin, value)).orElse(width(origin) / 2);
        final var height = effect.getHeight().map(value -> scaleVertical(origin, value)).orElse(height(origin));
        final var tick = animationTick(origin);
        final var speed = Math.max(0, effect.getSpeed());
        final var offset = samples == 1 ? 0 : (tick * speed) % samples;
        final var spirals = Math.max(1, effect.getSpirals());
        for (var spiral = 0; spiral < spirals; spiral++) for (var i = 0; i < count; i++) {
            final var sample = samples == 1 ? 0 : (offset + i) % samples;
            final var progress = samples == 1 ? .5 : (sample + .5) / samples;
            final var angle = Math.TAU * effect.getRotations() * progress + Math.TAU * spiral / spirals;
            final var y = (effect.isAscending() ? progress : 1 - progress) * height - height / 2;
            spawn(effect, player, point(origin, Math.cos(angle) * radius, y, Math.sin(angle) * radius));
        }
    }

    static void helix(final HelixEffect effect, final Player player, final Location origin) {
        final var strands = Math.max(1, effect.getStrands());
        final var count = particleCount(effect, origin, 8);
        final var radius = scaleHorizontal(origin, effect.getRadius());
        final var height = effect.getHeight().map(value -> scaleVertical(origin, value)).orElse(height(origin));
        for (var strand = 0; strand < strands; strand++) for (var i = 0; i < count; i++) {
            final var progress = i / (double) count;
            final var angle = Math.TAU * effect.getRotations() * progress + Math.TAU * strand / strands;
            spawn(effect, player, point(origin, Math.cos(angle) * radius, progress * height - height / 2 + .5, Math.sin(angle) * radius));
        }
    }

    static void pulse(final PulseEffect effect, final Player player, final Location origin) {
        final var waves = Math.max(1, effect.getWaves());
        for (var wave = 0; wave < waves; wave++) {
            final var radius = scaleHorizontal(origin, effect.getMinRadius() + (effect.getMaxRadius() - effect.getMinRadius()) * ((wave + 1) / (double) waves));
            ringLike(effect, player, origin, radius, effect.getShape());
        }
    }

    static void fountain(final FountainEffect effect, final Player player, final Location origin) {
        final var random = ThreadLocalRandom.current();
        final var baseRadius = scaleHorizontal(origin, effect.getBaseRadius());
        final var maxHeight = effect.getMaxHeight().map(value -> scaleVertical(origin, value)).orElse(height(origin));
        for (var i = 0; i < Math.max(1, effect.getSprayRate()); i++) {
            final var angle = random.nextDouble(Math.TAU);
            final var radius = random.nextDouble(Math.max(0, baseRadius));
            final var y = random.nextDouble(0, Math.max(0, maxHeight)) - maxHeight / 2;
            spawn(effect, player, point(origin, Math.cos(angle) * radius, y, Math.sin(angle) * radius));
        }
    }

    static void vortex(final VortexEffect effect, final Player player, final Location origin) {
        final var streams = Math.max(1, effect.getStreams());
        final var count = particleCount(effect, origin, 8);
        final var baseRadius = scaleHorizontal(origin, effect.getBaseRadius());
        final var topRadius = scaleHorizontal(origin, effect.getTopRadius());
        final var height = effect.getHeight().map(value -> scaleVertical(origin, value)).orElse(height(origin));
        for (var stream = 0; stream < streams; stream++) for (var i = 0; i < count; i++) {
            final var progress = i / (double) count;
            final var radius = baseRadius + (topRadius - baseRadius) * progress;
            final var direction = effect.isPullInward() ? 1 : -1;
            final var angle = Math.TAU * progress * direction + Math.TAU * stream / streams + Math.toRadians(effect.getRotationSpeed());
            spawn(effect, player, point(origin, Math.cos(angle) * radius, progress * height - height / 2 + .5, Math.sin(angle) * radius));
        }
    }

    static void waterfall(final WaterfallEffect effect, final Player player, final Location origin) {
        final var random = ThreadLocalRandom.current();
        final var width = effect.getWidth().map(value -> scaleHorizontal(origin, value) * 2).orElse(width(origin));
        final var height = effect.getHeight().map(value -> scaleVertical(origin, value)).orElse(height(origin));
        final var flowRate = Math.max(1, effect.getFlowRate().orElse((int) Math.ceil(width * 3)));
        final var tick = animationTick(origin);
        final var turbulence = Math.max(0, effect.getTurbulence());
        final var fallSpeed = effect.getFallSpeed().orElse(width / 10);
        final var fall = (tick * Math.max(0, fallSpeed)) % flowRate;
        for (var i = 0; i < flowRate; i++) {
            final var lane = flowRate == 1 ? .5 : i / (double) (flowRate - 1);
            final var sample = (i + fall) % flowRate;
            final var progress = flowRate == 1 ? .5 : Math.min(1, sample / (flowRate - 1));
            final var x = (lane - .5) * width + (random.nextDouble() - .5) * turbulence;
            final var y = height / 2 - progress * height + .5;
            final var z = (random.nextDouble() - .5) * turbulence;
            spawn(effect, player, point(origin, x, y, z));
        }
    }

    private static void ringLike(final PortalEffect effect, final Player player, final Location origin, final double radius, final PulseEffect.PulseShape shape) {
        final var count = particleCount(effect, origin, 12);
        for (var i = 0; i < count; i++) {
            final var progress = i / (double) count;
            final var angle = Math.TAU * progress;
            if (shape == PulseEffect.PulseShape.SQUARE || shape == PulseEffect.PulseShape.CUBE) {
                final var perimeter = progress * 4;
                final var side = (int) perimeter;
                final var local = perimeter - side;
                final var x = switch (side) {
                    case 0 -> -radius + local * radius * 2;
                    case 1 -> radius;
                    case 2 -> radius - local * radius * 2;
                    default -> -radius;
                };
                final var z = switch (side) {
                    case 0 -> -radius;
                    case 1 -> -radius + local * radius * 2;
                    case 2 -> radius;
                    default -> radius - local * radius * 2;
                };
                if (shape == PulseEffect.PulseShape.CUBE) spawnCube(effect, player, origin, radius, x, z);
                else spawn(effect, player, point(origin, x, 0, z));
                continue;
            }
            spawn(effect, player, point(origin, Math.cos(angle) * radius, shape == PulseEffect.PulseShape.SPHERE ? Math.sin(angle) * radius : 0, Math.sin(angle) * radius));
        }
    }

    private static void spawnCube(final PortalEffect effect, final Player player, final Location origin, final double radius, final double x, final double z) {
        spawn(effect, player, point(origin, x, -radius, z));
        spawn(effect, player, point(origin, x, radius, z));
        if (Math.abs(Math.abs(x) - radius) < .0001 && Math.abs(Math.abs(z) - radius) < .0001) {
            spawn(effect, player, point(origin, x, 0, z));
        }
    }

    static Location point(final Location origin, final double along, final double y, final double depth) {
        return Math.abs(origin.getYaw()) == 90f
                ? origin.clone().add(depth, y, along)
                : origin.clone().add(along, y, depth);
    }

    private static double scaleHorizontal(final Location origin, final double configured) {
        return configured <= 1 ? Math.max(0, configured) * width(origin) / 2 : configured;
    }

    private static double scaleVertical(final Location origin, final double configured) {
        return configured <= 1 ? Math.max(0, configured) * height(origin) : configured;
    }

    static double width(final Location origin) {
        return origin instanceof PortalEffectOrigin effectOrigin ? Math.max(1, effectOrigin.width()) : 1;
    }

    static double height(final Location origin) {
        return origin instanceof PortalEffectOrigin effectOrigin ? Math.max(1, effectOrigin.height()) : 1;
    }

    static long animationTick(final Location origin) {
        return origin instanceof PortalEffectOrigin effectOrigin ? effectOrigin.animationTick() : 0;
    }

    private static int particleCount(final PortalEffect effect, final Location origin, final double particlesPerBlock) {
        return Math.max(1, effect.getParticleCount().orElse((int) Math.ceil((width(origin) + height(origin)) * particlesPerBlock)));
    }

    private static void spawn(final PortalEffect effect, final Player player, final Location location) {
        final var data = data(effect);
        if (data == null && effect.getParticle().getDataType() != Void.class) return;
        if (data == null) {
            player.spawnParticle(effect.getParticle(), location, 1, 0, 0, 0, 0);
            return;
        }
        player.spawnParticle(effect.getParticle(), location, 1, 0, 0, 0, 0, data);
    }

    private static Object data(final PortalEffect effect) {
        if (effect.getColor().isEmpty() || effect.getParticle().getDataType() != Particle.DustOptions.class) return null;
        return new Particle.DustOptions(effect.getColor().orElseThrow(), 1f);
    }
}
