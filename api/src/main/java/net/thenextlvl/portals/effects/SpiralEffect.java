package net.thenextlvl.portals.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectTypeRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * An immutable spiral effect configuration that creates particles in a spiral pattern.
 * All properties are defined via the builder and cannot be changed after creation.
 */
public interface SpiralEffect extends PortalEffect {
    /**
     * Gets the radius of the spiral.
     *
     * @return the radius, or empty to infer it from the portal width
     */
    @Contract(pure = true)
    Optional<Double> getRadius();

    /**
     * Gets the height of the spiral.
     *
     * @return the height, or empty to infer it from the portal height
     */
    @Contract(pure = true)
    Optional<Double> getHeight();

    /**
     * Gets the number of rotations.
     *
     * @return the rotation count
     */
    @Contract(pure = true)
    double getRotations();

    /**
     * Gets the number of spiral strands shown at the same time.
     *
     * @return the spiral strand count
     */
    @Contract(pure = true)
    int getSpirals();

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

    SpiralEffect.Builder toBuilder();

    /**
     * Creates a new builder for spiral effects.
     *
     * @return a new spiral effect builder
     */
    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        return PortalEffectTypeRegistry.registry().builder(Builder.class).orElseThrow();
    }

    /**
     * Builder for creating immutable spiral effect configurations.
     */
    interface Builder extends PortalEffect.Builder<SpiralEffect, Builder> {
        /**
         * Sets the radius of the spiral.
         *
         * @param radius the radius, or null to infer it from the portal width
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder radius(@Nullable Double radius);

        /**
         * Sets the height of the spiral.
         *
         * @param height the height, or null to infer it from the portal height
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Nullable Double height);

        /**
         * Sets the number of rotations.
         *
         * @param rotations the rotation count
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder rotations(double rotations);

        /**
         * Sets the number of spiral strands shown at the same time.
         *
         * @param spirals the spiral strand count
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder spirals(int spirals);

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
