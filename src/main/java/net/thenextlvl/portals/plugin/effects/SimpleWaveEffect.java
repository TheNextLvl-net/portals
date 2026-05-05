package net.thenextlvl.portals.plugin.effects;

import net.thenextlvl.portals.effect.SimplePortalEffect;
import net.thenextlvl.portals.effects.WaveEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class SimpleWaveEffect extends SimplePortalEffect implements WaveEffect {
    private final WaveType waveType;
    private final boolean horizontal;
    private final double amplitude;
    private final double length;
    private final double waveSpeed;
    private final double wavelength;

    private SimpleWaveEffect(final Builder builder) throws IllegalArgumentException {
        super(builder);
        this.amplitude = builder.amplitude;
        this.wavelength = builder.wavelength;
        this.waveSpeed = builder.waveSpeed;
        this.waveType = builder.waveType;
        this.horizontal = builder.horizontal;
        this.length = builder.length;
    }

    @Override
    public double getAmplitude() {
        return amplitude;
    }

    @Override
    public double getWavelength() {
        return wavelength;
    }

    @Override
    public double getWaveSpeed() {
        return waveSpeed;
    }

    @Override
    public WaveType getWaveType() {
        return waveType;
    }

    @Override
    public boolean isHorizontal() {
        return horizontal;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public void play(final Player player, final Location origin) {
        final var samples = Math.max(1, getParticleCount());
        final var span = getLength() <= 1 ? Math.max(0, getLength()) * EffectRenderer.width(origin) : Math.max(0, getLength());
        final var step = samples == 1 ? 0 : span / (samples - 1);
        final var start = -span / 2d;
        final var phase = getWaveSpeed() * getSpeed();
        final var data = particleData();
        if (data == null && getParticle().getDataType() != Void.class) return;

        for (var i = 0; i < samples; i++) {
            final var x = start + step * i;
            final var wave = sample(x + phase) * getAmplitude();
            final var location = isHorizontal()
                    ? EffectRenderer.point(origin, x, 0, wave)
                    : EffectRenderer.point(origin, x, wave, 0);
            spawn(player, location, data);
        }
    }

    private double sample(final double input) {
        final var wavelength = Math.max(0.0001, getWavelength());
        final var angle = (input / wavelength) * Math.PI * 2d;
        return switch (getWaveType()) {
            case SINE -> Math.sin(angle);
            case SQUARE -> Math.sin(angle) >= 0 ? 1d : -1d;
            case TRIANGLE -> (2d / Math.PI) * Math.asin(Math.sin(angle));
            case SAWTOOTH -> 2d * (input / wavelength - Math.floor(0.5d + input / wavelength));
        };
    }

    private Object particleData() {
        if (getColor().isEmpty()) return null;
        if (getParticle().getDataType() != Particle.DustOptions.class) return null;
        return new Particle.DustOptions(getColor().orElseThrow(), 1f);
    }

    private void spawn(final Player player, final Location location, final Object data) {
        if (data == null) {
            player.spawnParticle(getParticle(), location, 1, 0, 0, 0, 0);
            return;
        }
        player.spawnParticle(getParticle(), location, 1, 0, 0, 0, 0, data);
    }

    @Override
    public WaveEffect.Builder toBuilder() {
        return new SimpleWaveEffect.Builder()
                .duration(getDuration())
                .particle(getParticle())
                .color(getColor().orElse(null))
                .particleCount(getParticleCount())
                .updateInterval(getUpdateInterval())
                .speed(getSpeed())
                .amplitude(getAmplitude())
                .wavelength(getWavelength())
                .waveSpeed(getWaveSpeed())
                .waveType(getWaveType())
                .horizontal(isHorizontal())
                .length(getLength());
    }

    public static final class Builder extends SimplePortalEffect.Builder<WaveEffect, WaveEffect.Builder> implements WaveEffect.Builder {
        private WaveType waveType = WaveType.SINE;
        private boolean horizontal = true;
        private double amplitude = 1.0;
        private double length = 1.0;
        private double waveSpeed = 1.0;
        private double wavelength = 1.0;

        @Override
        public WaveEffect.Builder amplitude(final double amplitude) {
            this.amplitude = amplitude;
            return this;
        }

        @Override
        public WaveEffect.Builder wavelength(final double wavelength) {
            this.wavelength = wavelength;
            return this;
        }

        @Override
        public WaveEffect.Builder waveSpeed(final double speed) {
            this.waveSpeed = speed;
            return this;
        }

        @Override
        public WaveEffect.Builder waveType(final WaveType type) {
            this.waveType = type;
            return this;
        }

        @Override
        public WaveEffect.Builder horizontal(final boolean horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        @Override
        public WaveEffect.Builder length(final double length) {
            this.length = length;
            return this;
        }

        @Override
        public WaveEffect build() throws IllegalArgumentException {
            return new SimpleWaveEffect(this);
        }
    }
}
