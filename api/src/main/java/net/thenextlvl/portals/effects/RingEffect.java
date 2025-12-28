package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.jetbrains.annotations.Contract;

/**
 * An immutable ring effect configuration that creates circular particle patterns.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface RingEffect extends PortalEffect {
    /**
     * Gets the radius of the ring.
     *
     * @return the radius
     */
    @Contract(pure = true)
    double getRadius();

    /**
     * Gets whether the ring is horizontal or vertical.
     *
     * @return true if horizontal, false if vertical
     */
    @Contract(pure = true)
    boolean isHorizontal();

    /**
     * Gets the rotation speed in degrees per tick.
     *
     * @return the rotation speed
     */
    @Contract(pure = true)
    double getRotationSpeed();

    /**
     * Gets the thickness of the ring.
     *
     * @return the thickness
     */
    @Contract(pure = true)
    double getThickness();

    /**
     * Gets whether the ring should pulse (expand/contract).
     *
     * @return true if pulsing is enabled
     */
    @Contract(pure = true)
    boolean isPulse();

    /**
     * Gets the pulse speed if pulsing is enabled.
     *
     * @return the pulse speed
     */
    @Contract(pure = true)
    double getPulseSpeed();

    RingEffect.Builder toBuilder();

    /**
     * Creates a new builder for ring effects.
     *
     * @return a new ring effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable ring effect configurations.
     */
    interface Builder extends PortalEffect.Builder<RingEffect, Builder> {
        /**
         * Sets the radius of the ring.
         *
         * @param radius the radius
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder radius(double radius);

        /**
         * Sets the ring orientation.
         *
         * @param horizontal true for horizontal, false for vertical
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder horizontal(boolean horizontal);

        /**
         * Sets the rotation speed in degrees per tick.
         *
         * @param speed the rotation speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotationSpeed(double speed);

        /**
         * Sets the thickness of the ring.
         *
         * @param thickness the thickness
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder thickness(double thickness);

        /**
         * Sets whether the ring should pulse (expand/contract).
         *
         * @param pulse true to enable pulsing
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pulse(boolean pulse);

        /**
         * Sets the pulse speed if pulsing is enabled.
         *
         * @param speed the pulse speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pulseSpeed(double speed);
    }
}
