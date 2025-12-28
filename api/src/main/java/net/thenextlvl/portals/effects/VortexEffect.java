package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable vortex effect configuration that creates a swirling tornado-like pattern.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface VortexEffect extends PortalEffect {
    /**
     * Gets the base radius of the vortex.
     *
     * @return the base radius
     */
    @Contract(pure = true)
    double getBaseRadius();

    /**
     * Gets the top radius of the vortex.
     *
     * @return the top radius
     */
    @Contract(pure = true)
    double getTopRadius();

    /**
     * Gets the height of the vortex.
     *
     * @return the height
     */
    @Contract(pure = true)
    double getHeight();

    /**
     * Gets the rotation speed.
     *
     * @return the rotation speed
     */
    @Contract(pure = true)
    double getRotationSpeed();

    /**
     * Gets the number of particle streams.
     *
     * @return the number of streams
     */
    @Contract(pure = true)
    int getStreams();

    /**
     * Gets whether particles pull inward or push outward.
     *
     * @return true for inward pull, false for outward push
     */
    @Contract(pure = true)
    boolean isPullInward();

    /**
     * Creates a new builder for vortex effects.
     *
     * @return a new vortex effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable vortex effect configurations.
     */
    interface Builder extends PortalEffect.Builder<VortexEffect, Builder> {
        /**
         * Sets the base radius of the vortex.
         *
         * @param radius the base radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder baseRadius(double radius);

        /**
         * Sets the top radius of the vortex.
         *
         * @param radius the top radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder topRadius(double radius);

        /**
         * Sets the height of the vortex.
         *
         * @param height the height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(double height);

        /**
         * Sets the rotation speed in degrees per tick.
         *
         * @param speed the rotation speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotationSpeed(double speed);

        /**
         * Sets the number of particle streams.
         *
         * @param streams the number of streams
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder streams(int streams);

        /**
         * Sets whether particles pull inward or push outward.
         *
         * @param inward true for inward pull, false for outward push
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pullInward(boolean inward);
    }
}
