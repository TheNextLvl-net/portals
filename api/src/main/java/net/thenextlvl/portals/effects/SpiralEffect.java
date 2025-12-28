package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable spiral effect configuration that creates particles in a spiral pattern.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface SpiralEffect extends PortalEffect {
    /**
     * Gets the radius of the spiral.
     *
     * @return the radius
     */
    @Contract(pure = true)
    double getRadius();

    /**
     * Gets the height of the spiral.
     *
     * @return the height
     */
    @Contract(pure = true)
    double getHeight();

    /**
     * Gets the number of rotations.
     *
     * @return the rotation count
     */
    @Contract(pure = true)
    double getRotations();

    /**
     * Gets whether the spiral ascends or descends.
     *
     * @return true if ascending, false if descending
     */
    @Contract(pure = true)
    boolean isAscending();

    /**
     * Gets the particle density (particles per rotation).
     *
     * @return the density
     */
    @Contract(pure = true)
    int getDensity();

    /**
     * Gets the rotation speed in degrees per tick.
     *
     * @return the rotation speed
     */
    @Contract(pure = true)
    double getRotationSpeed();

    /**
     * Creates a new builder for spiral effects.
     *
     * @return a new spiral effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable spiral effect configurations.
     */
    interface Builder extends PortalEffect.Builder<SpiralEffect, Builder> {
        /**
         * Sets the radius of the spiral.
         *
         * @param radius the radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder radius(double radius);

        /**
         * Sets the height of the spiral.
         *
         * @param height the height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(double height);

        /**
         * Sets the number of rotations.
         *
         * @param rotations the rotation count
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotations(double rotations);

        /**
         * Sets the spiral direction.
         *
         * @param ascending true for ascending, false for descending
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder ascending(boolean ascending);

        /**
         * Sets the tightness of the spiral (particles per rotation).
         *
         * @param density the particle density
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder density(int density);

        /**
         * Sets the rotation speed in degrees per tick.
         *
         * @param speed the rotation speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotationSpeed(double speed);
    }
}
