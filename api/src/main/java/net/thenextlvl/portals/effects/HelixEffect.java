package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

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
     * @return the height
     */
    @Contract(pure = true)
    double getHeight();

    /**
     * Gets the number of strands in the helix.
     *
     * @return the strand count
     */
    @Contract(pure = true)
    int getStrands();

    /**
     * Gets the rotation speed in degrees per tick.
     *
     * @return the rotation speed
     */
    @Contract(pure = true)
    double getRotationSpeed();

    /**
     * Gets the pitch (tightness) of the helix.
     *
     * @return the pitch value
     */
    @Contract(pure = true)
    double getPitch();

    HelixEffect.Builder toBuilder();

    /**
     * Creates a new builder for helix effects.
     *
     * @return a new helix effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
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
         * @param height the height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(double height);

        /**
         * Sets the number of intertwining strands.
         *
         * @param strands the strand count
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder strands(int strands);

        /**
         * Sets the rotation speed in degrees per tick.
         *
         * @param speed the rotation speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotationSpeed(double speed);

        /**
         * Sets the pitch (tightness) of the helix.
         *
         * @param pitch the pitch value
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pitch(double pitch);
    }
}
