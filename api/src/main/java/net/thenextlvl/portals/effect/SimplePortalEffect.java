package net.thenextlvl.portals.effect;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;

public abstract class SimplePortalEffect implements PortalEffect {
    private final @Nullable Color color;
    private final Duration duration;
    private final Duration updateInterval;
    private final Particle particle;
    private final double speed;
    private final int particleCount;

    protected SimplePortalEffect(final Builder<?, ?> builder) throws IllegalArgumentException {
        Preconditions.checkArgument(builder.particle != null, "Particle cannot be null");
        this.color = builder.color;
        this.duration = builder.duration;
        this.particle = builder.particle;
        this.particleCount = builder.particleCount;
        this.speed = builder.speed;
        this.updateInterval = builder.updateInterval;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public Particle getParticle() {
        return particle;
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

    @Override
    public int getParticleCount() {
        return particleCount;
    }

    @Override
    public Duration getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<T extends PortalEffect, B extends PortalEffect.Builder<T, B>> implements PortalEffect.Builder<T, B> {
        protected @Nullable Color color = null;
        protected @Nullable Particle particle = null;
        protected Duration duration = Duration.ofSeconds(5);
        protected Duration updateInterval = Duration.ofMillis(50);
        protected double speed = 1.0;
        protected int particleCount = 10;

        @Override
        public B duration(final Duration duration) {
            this.duration = duration;
            return (B) this;
        }

        @Override
        public B particle(final Particle particle) {
            this.particle = particle;
            return (B) this;
        }

        @Override
        public B color(final @Nullable Color color) {
            this.color = color;
            return (B) this;
        }

        @Override
        public B particleCount(final int count) {
            this.particleCount = count;
            return (B) this;
        }

        @Override
        public B updateInterval(final Duration interval) {
            this.updateInterval = interval;
            return (B) this;
        }

        @Override
        public B speed(final double speed) {
            this.speed = speed;
            return (B) this;
        }
    }
}
