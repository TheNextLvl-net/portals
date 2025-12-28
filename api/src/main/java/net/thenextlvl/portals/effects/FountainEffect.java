package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable fountain effect configuration that shoots particles upward and lets them fall.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface FountainEffect extends PortalEffect {
    /**
     * Gets the base radius of the fountain.
     *
     * @return the base radius
     */
    @Contract(pure = true)
    double getBaseRadius();

    /**
     * Gets the maximum height particles reach.
     *
     * @return the max height
     */
    @Contract(pure = true)
    double getMaxHeight();

    /**
     * Gets the spray rate (particles per tick).
     *
     * @return the spray rate
     */
    @Contract(pure = true)
    int getSprayRate();

    /**
     * Gets the initial velocity of particles.
     *
     * @return the initial velocity
     */
    @Contract(pure = true)
    double getVelocity();

    /**
     * Gets the spread angle in degrees.
     *
     * @return the spread angle
     */
    @Contract(pure = true)
    double getSpreadAngle();

    /**
     * Gets whether particles should arc or shoot straight up.
     *
     * @return true for arcing particles
     */
    @Contract(pure = true)
    boolean isArc();

    /**
     * Creates a new builder for fountain effects.
     *
     * @return a new fountain effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable fountain effect configurations.
     */
    interface Builder extends PortalEffect.Builder<FountainEffect, Builder> {
        /**
         * Sets the base radius of the fountain.
         *
         * @param radius the base radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder baseRadius(double radius);

        /**
         * Sets the maximum height particles reach.
         *
         * @param height the max height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder maxHeight(double height);

        /**
         * Sets the spray rate (particles per tick).
         *
         * @param rate the spray rate
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sprayRate(int rate);

        /**
         * Sets the initial velocity of particles.
         *
         * @param velocity the initial velocity
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder velocity(double velocity);

        /**
         * Sets the spread angle in degrees.
         *
         * @param angle the spread angle
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder spreadAngle(double angle);

        /**
         * Sets whether particles should arc or shoot straight up.
         *
         * @param arc true for arcing particles
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder arc(boolean arc);
    }
}
