package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable pulse effect configuration that creates expanding/contracting particle waves.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface PulseEffect extends PortalEffect {
    /**
     * Gets the minimum radius of the pulse.
     *
     * @return the minimum radius
     */
    @Contract(pure = true)
    double getMinRadius();

    /**
     * Gets the maximum radius of the pulse.
     *
     * @return the maximum radius
     */
    @Contract(pure = true)
    double getMaxRadius();

    /**
     * Gets the pulse speed.
     *
     * @return the pulse speed
     */
    @Contract(pure = true)
    double getPulseSpeed();

    /**
     * Gets the shape of the pulse.
     *
     * @return the pulse shape
     */
    @Contract(pure = true)
    PulseShape getShape();

    /**
     * Gets whether the pulse should fade as it expands.
     *
     * @return true if fading is enabled
     */
    @Contract(pure = true)
    boolean isFade();

    /**
     * Gets the number of concurrent pulse waves.
     *
     * @return the number of waves
     */
    @Contract(pure = true)
    int getWaves();

    /**
     * Creates a new builder for pulse effects.
     *
     * @return a new pulse effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable pulse effect configurations.
     */
    interface Builder extends PortalEffect.Builder<PulseEffect, Builder> {
        /**
         * Sets the minimum radius of the pulse.
         *
         * @param radius the minimum radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder minRadius(double radius);

        /**
         * Sets the maximum radius of the pulse.
         *
         * @param radius the maximum radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder maxRadius(double radius);

        /**
         * Sets the pulse speed.
         *
         * @param speed the pulse speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pulseSpeed(double speed);

        /**
         * Sets the shape of the pulse.
         *
         * @param shape the pulse shape
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder shape(PulseShape shape);

        /**
         * Sets whether the pulse should fade as it expands.
         *
         * @param fade true to enable fading
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder fade(boolean fade);

        /**
         * Sets the number of concurrent pulse waves.
         *
         * @param waves the number of waves
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder waves(int waves);
    }

    /**
     * Shapes for pulse effects.
     */
    enum PulseShape {
        CIRCLE,
        SQUARE,
        SPHERE,
        CUBE
    }
}
