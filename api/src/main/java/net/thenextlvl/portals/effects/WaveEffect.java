package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable wave effect configuration that creates rippling or undulating particle patterns.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface WaveEffect extends PortalEffect {
    /**
     * Gets the amplitude of the wave.
     *
     * @return the amplitude
     */
    @Contract(pure = true)
    double getAmplitude();

    /**
     * Gets the wavelength.
     *
     * @return the wavelength
     */
    @Contract(pure = true)
    double getWavelength();

    /**
     * Gets the wave speed.
     *
     * @return the wave speed
     */
    @Contract(pure = true)
    double getWaveSpeed();

    /**
     * Gets the wave type.
     *
     * @return the wave type
     */
    @Contract(pure = true)
    WaveType getWaveType();

    /**
     * Gets whether the wave is horizontal or vertical.
     *
     * @return true if horizontal, false if vertical
     */
    @Contract(pure = true)
    boolean isHorizontal();

    /**
     * Gets the width/length of the wave.
     *
     * @return the length
     */
    @Contract(pure = true)
    double getLength();

    /**
     * Creates a new builder for wave effects.
     *
     * @return a new wave effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable wave effect configurations.
     */
    interface Builder extends PortalEffect.Builder<WaveEffect, Builder> {
        /**
         * Sets the amplitude of the wave.
         *
         * @param amplitude the amplitude
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder amplitude(double amplitude);

        /**
         * Sets the wavelength.
         *
         * @param wavelength the wavelength
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder wavelength(double wavelength);

        /**
         * Sets the wave speed.
         *
         * @param speed the wave speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder waveSpeed(double speed);

        /**
         * Sets the wave type (sine, square, sawtooth, etc.).
         *
         * @param type the wave type
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder waveType(WaveType type);

        /**
         * Sets whether the wave is horizontal or vertical.
         *
         * @param horizontal true for horizontal, false for vertical
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder horizontal(boolean horizontal);

        /**
         * Sets the width/length of the wave.
         *
         * @param length the length
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder length(double length);
    }

    /**
     * Types of wave patterns.
     */
    enum WaveType {
        SINE,
        SQUARE,
        TRIANGLE,
        SAWTOOTH
    }
}
