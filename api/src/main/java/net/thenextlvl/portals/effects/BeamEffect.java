package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;

/**
 * An immutable beam effect configuration that creates a particle beam between two points.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface BeamEffect extends PortalEffect {
    /**
     * Gets the start location of the beam.
     *
     * @return the start location
     */
    @Contract(pure = true)
    Location getStart();

    /**
     * Gets the end location of the beam.
     *
     * @return the end location
     */
    @Contract(pure = true)
    Location getEnd();

    /**
     * Gets the thickness of the beam.
     *
     * @return the thickness
     */
    @Contract(pure = true)
    double getThickness();

    /**
     * Gets the particle density along the beam.
     *
     * @return the density
     */
    @Contract(pure = true)
    int getDensity();

    /**
     * Gets whether the beam should animate/flow.
     *
     * @return true if animation is enabled
     */
    @Contract(pure = true)
    boolean isAnimate();

    /**
     * Gets the animation speed if animation is enabled.
     *
     * @return the animation speed
     */
    @Contract(pure = true)
    double getAnimationSpeed();

    BeamEffect.Builder toBuilder();

    /**
     * Creates a new builder for beam effects.
     *
     * @return a new beam effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Builder for creating immutable beam effect configurations.
     */
    interface Builder extends PortalEffect.Builder<BeamEffect, Builder> {
        /**
         * Sets the start location of the beam.
         *
         * @param location the start location
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder start(Location location);

        /**
         * Sets the end location of the beam.
         *
         * @param location the end location
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder end(Location location);

        /**
         * Sets the thickness of the beam.
         *
         * @param thickness the thickness
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder thickness(double thickness);

        /**
         * Sets the particle density along the beam.
         *
         * @param density the density
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder density(int density);

        /**
         * Sets whether the beam should animate/flow.
         *
         * @param animate true to enable animation
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder animate(boolean animate);

        /**
         * Sets the animation speed if animation is enabled.
         *
         * @param speed the animation speed
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder animationSpeed(double speed);
    }
}
