package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectTypeRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

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
     * @return the max height, or empty to infer it from the portal height
     */
    @Contract(pure = true)
    Optional<Double> getMaxHeight();

    /**
     * Gets the spray rate (particles per tick).
     *
     * @return the spray rate
     */
    @Contract(pure = true)
    int getSprayRate();

    FountainEffect.Builder toBuilder();

    /**
     * Creates a new builder for fountain effects.
     *
     * @return a new fountain effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        return PortalEffectTypeRegistry.registry().builder(Builder.class).orElseThrow();
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
         * @param height the max height, or null to infer it from the portal height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder maxHeight(@Nullable Double height);

        /**
         * Sets the spray rate (particles per tick).
         *
         * @param rate the spray rate
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sprayRate(int rate);

    }
}
