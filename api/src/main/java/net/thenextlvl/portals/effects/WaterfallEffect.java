package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectTypeRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * An immutable waterfall effect configuration that creates cascading particles.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface WaterfallEffect extends PortalEffect {
    /**
     * Gets the width of the waterfall.
     *
     * @return the width, or empty to infer it from the portal width
     */
    @Contract(pure = true)
    Optional<Double> getWidth();

    /**
     * Gets the height of the waterfall.
     *
     * @return the height, or empty to infer it from the portal height
     */
    @Contract(pure = true)
    Optional<Double> getHeight();

    /**
     * Gets the flow rate (particles per tick).
     *
     * @return the flow rate, or empty to infer it from the portal width
     */
    @Contract(pure = true)
    OptionalInt getFlowRate();

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
     * @return the fall speed, or empty to infer it from the portal width
     */
    @Contract(pure = true)
    Optional<Double> getFallSpeed();

    WaterfallEffect.Builder toBuilder();

    /**
     * Creates a new builder for waterfall effects.
     *
     * @return a new waterfall effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        return PortalEffectTypeRegistry.registry().builder(Builder.class).orElseThrow();
    }

    /**
     * Builder for creating immutable waterfall effect configurations.
     */
    interface Builder extends PortalEffect.Builder<WaterfallEffect, Builder> {
        /**
         * Sets the width of the waterfall.
         *
         * @param width the width, or null to infer it from the portal width
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(@Nullable Double width);

        /**
         * Sets the height of the waterfall.
         *
         * @param height the height, or null to infer it from the portal height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Nullable Double height);

        /**
         * Sets the flow rate (particles per tick).
         *
         * @param rate the flow rate, or null to infer it from the portal width
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder flowRate(@Nullable Integer rate);

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
         * @param speed the fall speed, or null to infer it from the portal width
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder fallSpeed(@Nullable Double speed);
    }
}
