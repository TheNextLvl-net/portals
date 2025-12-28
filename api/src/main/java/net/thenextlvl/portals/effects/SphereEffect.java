package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable sphere effect configuration that creates spherical particle patterns.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface SphereEffect extends PortalEffect {
    /**
     * Gets the radius of the sphere.
     *
     * @return the radius
     */
    @Contract(pure = true)
    double getRadius();

    /**
     * Gets whether the sphere is filled or hollow.
     *
     * @return true if filled, false if hollow
     */
    @Contract(pure = true)
    boolean isFilled();

    /**
     * Gets the particle density.
     *
     * @return the density
     */
    @Contract(pure = true)
    int getDensity();

    /**
     * Gets whether the sphere should rotate.
     *
     * @return true if rotation is enabled
     */
    @Contract(pure = true)
    boolean isRotate();

    /**
     * Gets the rotation speed if rotation is enabled.
     *
     * @return the rotation speed
     */
    @Contract(pure = true)
    double getRotationSpeed();

    /**
     * Gets whether the sphere should pulse (expand/contract).
     *
     * @return true if pulsing is enabled
     */
    @Contract(pure = true)
    boolean isPulse();

    /**
     * Creates a new builder for sphere effects.
     *
     * @return a new sphere effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable sphere effect configurations.
     */
    interface Builder extends PortalEffect.Builder<SphereEffect, Builder> {
        /**
         * Sets the radius of the sphere.
         *
         * @param radius the radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder radius(double radius);

        /**
         * Sets whether the sphere is filled or hollow.
         *
         * @param filled true for filled, false for hollow
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder filled(boolean filled);

        /**
         * Sets the particle density.
         *
         * @param density the density
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder density(int density);

        /**
         * Sets whether the sphere should rotate.
         *
         * @param rotate true to enable rotation
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotate(boolean rotate);

        /**
         * Sets the rotation speed if rotation is enabled.
         *
         * @param speed the rotation speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotationSpeed(double speed);

        /**
         * Sets whether the sphere should pulse (expand/contract).
         *
         * @param pulse true to enable pulsing
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pulse(boolean pulse);
    }
}
