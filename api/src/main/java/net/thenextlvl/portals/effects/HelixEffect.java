package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectTypeRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * An immutable helix effect configuration that creates intertwining spiral patterns.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface HelixEffect extends PortalEffect {
    /**
     * Gets the radius of the helix.
     *
     * @return the radius
     */
    @Contract(pure = true)
    double getRadius();

    /**
     * Gets the height of the helix.
     *
     * @return the height, or empty to infer it from the portal height
     */
    @Contract(pure = true)
    Optional<Double> getHeight();

    /**
     * Gets the number of strands in the helix.
     *
     * @return the strand count
     */
    @Contract(pure = true)
    int getStrands();

    /**
     * Gets how many times the helix revolves over its height.
     *
     * @return the rotation count
     */
    @Contract(pure = true)
    double getRotations();

    HelixEffect.Builder toBuilder();

    /**
     * Creates a new builder for helix effects.
     *
     * @return a new helix effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        return PortalEffectTypeRegistry.registry().builder(Builder.class).orElseThrow();
    }

    /**
     * Builder for creating immutable helix effect configurations.
     */
    interface Builder extends PortalEffect.Builder<HelixEffect, Builder> {
        /**
         * Sets the radius of the helix.
         *
         * @param radius the radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder radius(double radius);

        /**
         * Sets the height of the helix.
         *
         * @param height the height, or null to infer it from the portal height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Nullable Double height);

        /**
         * Sets the number of intertwining strands.
         *
         * @param strands the strand count
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder strands(int strands);

        /**
         * Sets how many times the helix revolves over its height.
         *
         * @param rotations the rotation count
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotations(double rotations);
    }
}
