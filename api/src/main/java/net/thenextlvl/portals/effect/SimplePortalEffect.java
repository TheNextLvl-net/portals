package net.thenextlvl.portals.effect;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public abstract class SimplePortalEffect implements PortalEffect {
    private final @Nullable Color color;
    private final Particle particle;
    private final double speed;
    private final @Nullable Integer particleCount;

    protected SimplePortalEffect(final Builder<?, ?> builder) throws IllegalArgumentException {
        Preconditions.checkArgument(builder.particle != null, "Particle cannot be null");
        this.color = builder.color;
        this.particle = builder.particle;
        this.particleCount = builder.particleCount;
        this.speed = builder.speed;
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
    public OptionalInt getParticleCount() {
        return particleCount != null ? OptionalInt.of(particleCount) : OptionalInt.empty();
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    protected <T extends PortalEffect, B extends PortalEffect.Builder<T, B>> B copyBase(final B builder) {
        return builder.particle(getParticle())
                .color(getColor().orElse(null))
                .particleCount(getParticleCount().isPresent() ? getParticleCount().getAsInt() : null)
                .speed(getSpeed());
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<T extends PortalEffect, B extends PortalEffect.Builder<T, B>> implements PortalEffect.Builder<T, B> {
        protected @Nullable Color color = null;
        protected @Nullable Particle particle = null;
        protected double speed = 1.0;
        protected @Nullable Integer particleCount = null;

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
        public B particleCount(final @Nullable Integer count) {
            this.particleCount = count;
            return (B) this;
        }

        @Override
        public B speed(final double speed) {
            this.speed = speed;
            return (B) this;
        }
    }
}
