package net.thenextlvl.portals.plugin.effects;

import net.thenextlvl.portals.effect.PortalEffect;
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

import java.util.concurrent.ThreadLocalRandom;

@NullMarked
final class EffectRenderer {
    private EffectRenderer() {
    }

    static void beam(final BeamEffect effect, final Player player, final Location origin) {
        final var base = origin.clone();
        final var startOffset = effect.getStart();
        final var endOffset = effect.getEnd();
        final var start = point(base, startOffset.getX(), startOffset.getY(), startOffset.getZ());
        final var end = point(base, endOffset.getX(), endOffset.getY(), endOffset.getZ());
        final var count = Math.max(1, effect.getDensity());
        for (var i = 0; i < count; i++) {
            final var progress = count == 1 ? 0 : i / (double) (count - 1);
            final var location = start.clone().add(
                    (end.getX() - start.getX()) * progress,
                    (end.getY() - start.getY()) * progress,
                    (end.getZ() - start.getZ()) * progress
            );
            spawn(effect, player, location);
        }
    }

    static void ring(final RingEffect effect, final Player player, final Location origin) {
        final var count = Math.max(8, effect.getParticleCount());
        final var radius = scaleHorizontal(origin, Math.max(0, effect.getRadius() + (effect.isPulse() ? Math.sin(effect.getPulseSpeed()) * effect.getThickness() : 0)));
        for (var i = 0; i < count; i++) {
            final var angle = Math.TAU * i / count + Math.toRadians(effect.getRotationSpeed());
            final var x = Math.cos(angle) * radius;
            final var y = effect.isHorizontal() ? 0 : Math.sin(angle) * radius;
            final var z = effect.isHorizontal() ? Math.sin(angle) * radius : 0;
            spawn(effect, player, point(origin, x, y, z));
        }
    }

    static void sphere(final SphereEffect effect, final Player player, final Location origin) {
        final var count = Math.max(8, effect.getDensity());
        final var radius = Math.min(scaleHorizontal(origin, Math.max(0, effect.getRadius())), scaleVertical(origin, Math.max(0, effect.getRadius())));
        for (var i = 0; i < count; i++) {
            final var phi = Math.acos(1 - 2 * (i + .5) / count);
            final var theta = Math.PI * (1 + Math.sqrt(5)) * i + Math.toRadians(effect.isRotate() ? effect.getRotationSpeed() : 0);
            final var r = effect.isFilled() ? radius * ((i % count) / (double) count) : radius;
            spawn(effect, player, point(origin,
                    Math.cos(theta) * Math.sin(phi) * r,
                    Math.cos(phi) * r,
                    Math.sin(theta) * Math.sin(phi) * r
            ));
        }
    }

    static void spiral(final SpiralEffect effect, final Player player, final Location origin) {
        final var count = Math.max(1, (int) Math.ceil(effect.getDensity() * Math.max(1, effect.getRotations())));
        final var radius = scaleHorizontal(origin, effect.getRadius());
        final var height = scaleVertical(origin, effect.getHeight());
        for (var i = 0; i < count; i++) {
            final var progress = i / (double) count;
            final var angle = Math.TAU * effect.getRotations() * progress + Math.toRadians(effect.getRotationSpeed());
            final var y = (effect.isAscending() ? progress : 1 - progress) * height - height / 2;
            spawn(effect, player, point(origin, Math.cos(angle) * radius, y, Math.sin(angle) * radius));
        }
    }

    static void helix(final HelixEffect effect, final Player player, final Location origin) {
        final var strands = Math.max(1, effect.getStrands());
        final var count = Math.max(1, effect.getParticleCount());
        final var radius = scaleHorizontal(origin, effect.getRadius());
        final var height = scaleVertical(origin, effect.getHeight());
        for (var strand = 0; strand < strands; strand++) for (var i = 0; i < count; i++) {
            final var progress = i / (double) count;
            final var angle = Math.TAU * effect.getPitch() * progress + Math.TAU * strand / strands + Math.toRadians(effect.getRotationSpeed());
            spawn(effect, player, point(origin, Math.cos(angle) * radius, progress * height - height / 2, Math.sin(angle) * radius));
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
        final var maxHeight = scaleVertical(origin, effect.getMaxHeight());
        for (var i = 0; i < Math.max(1, effect.getSprayRate()); i++) {
            final var angle = random.nextDouble(Math.TAU);
            final var radius = random.nextDouble(Math.max(0, baseRadius));
            final var y = random.nextDouble(0, Math.max(0, maxHeight)) - maxHeight / 2;
            spawn(effect, player, point(origin, Math.cos(angle) * radius, y, Math.sin(angle) * radius));
        }
    }

    static void vortex(final VortexEffect effect, final Player player, final Location origin) {
        final var streams = Math.max(1, effect.getStreams());
        final var count = Math.max(1, effect.getParticleCount());
        final var baseRadius = scaleHorizontal(origin, effect.getBaseRadius());
        final var topRadius = scaleHorizontal(origin, effect.getTopRadius());
        final var height = scaleVertical(origin, effect.getHeight());
        for (var stream = 0; stream < streams; stream++) for (var i = 0; i < count; i++) {
            final var progress = i / (double) count;
            final var radius = baseRadius + (topRadius - baseRadius) * progress;
            final var direction = effect.isPullInward() ? 1 : -1;
            final var angle = Math.TAU * progress * direction + Math.TAU * stream / streams + Math.toRadians(effect.getRotationSpeed());
            spawn(effect, player, point(origin, Math.cos(angle) * radius, progress * height - height / 2, Math.sin(angle) * radius));
        }
    }

    static void waterfall(final WaterfallEffect effect, final Player player, final Location origin) {
        final var random = ThreadLocalRandom.current();
        final var width = scaleHorizontal(origin, effect.getWidth()) * 2;
        final var height = scaleVertical(origin, effect.getHeight());
        for (var i = 0; i < Math.max(1, effect.getFlowRate()); i++) {
            final var x = random.nextDouble(-width / 2, width / 2);
            final var y = random.nextDouble(-height / 2, height / 2);
            final var z = (random.nextDouble() - .5) * effect.getTurbulence();
            spawn(effect, player, point(origin, x, y, z));
        }
    }

    private static void ringLike(final PortalEffect effect, final Player player, final Location origin, final double radius, final PulseEffect.PulseShape shape) {
        final var count = Math.max(8, effect.getParticleCount());
        for (var i = 0; i < count; i++) {
            final var angle = Math.TAU * i / count;
            final var x = switch (shape) {
                case SQUARE, CUBE -> Math.copySign(radius, Math.cos(angle)) * Math.min(1, Math.abs(Math.cos(angle) / Math.max(.0001, Math.sin(angle))));
                default -> Math.cos(angle) * radius;
            };
            final var z = switch (shape) {
                case SQUARE, CUBE -> Math.copySign(radius, Math.sin(angle)) * Math.min(1, Math.abs(Math.sin(angle) / Math.max(.0001, Math.cos(angle))));
                default -> Math.sin(angle) * radius;
            };
            spawn(effect, player, point(origin, x, shape == PulseEffect.PulseShape.SPHERE || shape == PulseEffect.PulseShape.CUBE ? Math.sin(angle) * radius : 0, z));
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
