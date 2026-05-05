package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.SimplePortalEffect;
import org.bukkit.entity.Player;

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
    public void play(final Player player) {
        // todo: implement
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
