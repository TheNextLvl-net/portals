package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable waterfall effect configuration that creates cascading particles.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface WaterfallEffect extends PortalEffect {
    /**
     * Gets the width of the waterfall.
     *
     * @return the width
     */
    @Contract(pure = true)
    double getWidth();

    /**
     * Gets the height of the waterfall.
     *
     * @return the height
     */
    @Contract(pure = true)
    double getHeight();

    /**
     * Gets the flow rate (particles per tick).
     *
     * @return the flow rate
     */
    @Contract(pure = true)
    int getFlowRate();

    /**
     * Gets the splash radius at the bottom.
     *
     * @return the splash radius
     */
    @Contract(pure = true)
    double getSplashRadius();

    /**
     * Gets the turbulence/randomness of the flow.
     *
     * @return the turbulence factor (0.0 - 1.0)
     */
    @Contract(pure = true)
    double getTurbulence();

    /**
     * Gets the fall speed of particles.
     *
     * @return the fall speed
     */
    @Contract(pure = true)
    double getFallSpeed();

    WaterfallEffect.Builder toBuilder();

    /**
     * Creates a new builder for waterfall effects.
     *
     * @return a new waterfall effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable waterfall effect configurations.
     */
    interface Builder extends PortalEffect.Builder<WaterfallEffect, Builder> {
        /**
         * Sets the width of the waterfall.
         *
         * @param width the width
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(double width);

        /**
         * Sets the height of the waterfall.
         *
         * @param height the height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(double height);

        /**
         * Sets the flow rate (particles per tick).
         *
         * @param rate the flow rate
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder flowRate(int rate);

        /**
         * Sets the splash effect at the bottom.
         *
         * @param radius the splash radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder splash(double radius);

        /**
         * Sets the turbulence/randomness of the flow.
         *
         * @param turbulence the turbulence factor (0.0 - 1.0)
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder turbulence(double turbulence);

        /**
         * Sets the fall speed of particles.
         *
         * @param speed the fall speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder fallSpeed(double speed);
    }
}
