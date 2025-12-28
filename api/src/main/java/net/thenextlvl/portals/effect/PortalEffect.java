package net.thenextlvl.portals.effect;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.time.Duration;

/**
 * Immutable base interface for all portal particle effects.
 * This allows developers to create custom effect types by implementing this interface.
 * All effects are configured via builders and cannot be modified after creation.
 */
public interface PortalEffect {
    /**
     * Gets the duration of the effect.
     *
     * @return the effect duration
     */
    @Contract(pure = true)
    Duration getDuration();

    /**
     * Gets the particle type used by this effect.
     *
     * @return the particle type
     */
    @Contract(pure = true)
    Particle getParticle();

    /**
     * Gets the particle color (for colorable particles).
     *
     * @return the color, or null if not set
     */
    @Contract(pure = true)
    Color getColor();

    /**
     * Gets the particle count.
     *
     * @return the particle count
     */
    @Contract(pure = true)
    int getParticleCount();

    /**
     * Gets the update interval (how often particles spawn).
     *
     * @return the interval
     */
    @Contract(pure = true)
    Duration getUpdateInterval();

    /**
     * Gets the effect speed/velocity.
     *
     * @return the speed multiplier
     */
    @Contract(pure = true)
    double getSpeed();

    /**
     * Plays the effect for the specified player.
     *
     * @param player the player to play the effect for
     */
    @ApiStatus.OverrideOnly
    void play(Player player);

    /**
     * Base builder interface for effect configurations.
     *
     * @param <T> the type of effect config being built
     * @param <B> the type of builder (for fluent chaining)
     */
    interface Builder<T extends PortalEffect, B extends Builder<T, B>> {
        /**
         * Sets the effect duration.
         * This is the total time the effect will run when played.
         *
         * @param duration the duration
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        B duration(Duration duration);

        /**
         * Sets the particle type for the effect.
         *
         * @param particle the particle type
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        B particle(Particle particle);

        /**
         * Sets the particle color (for colorable particles).
         *
         * @param color the color
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        B color(Color color);

        /**
         * Sets the particle count.
         *
         * @param count the number of particles
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        B particleCount(int count);

        /**
         * Sets the update interval (how often particles spawn).
         *
         * @param interval the interval
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        B updateInterval(Duration interval);

        /**
         * Sets the effect speed/velocity.
         *
         * @param speed the speed multiplier
         * @return this builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        B speed(double speed);

        /**
         * Builds the immutable effect configuration.
         *
         * @return the constructed effect config
         */
        @Contract(value = " -> new", pure = true)
        T build();
    }
}
