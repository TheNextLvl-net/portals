package net.thenextlvl.portals.bounds;

import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.key.Key;
import net.thenextlvl.binder.StaticBinder;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a bounding box for teleportation.
 *
 * @since 0.2.0
 */
@ApiStatus.NonExtendable
public interface Bounds {
    /**
     * Gets the factory for creating Bounds instances.
     *
     * @return The bounds factory.
     * @since 0.2.0
     */
    static @CheckReturnValue BoundsFactory factory() {
        return StaticBinder.getInstance(BoundsFactory.class.getClassLoader()).find(BoundsFactory.class);
    }

    /**
     * Gets the world the bounds are in.
     * <p>
     * May be empty if the world is not loaded.
     *
     * @return The world.
     * @since 0.2.0
     */
    Optional<World> world();

    /**
     * Gets the world key.
     *
     * @return The world key.
     * @since 0.2.0
     */
    @Contract(pure = true)
    Key worldKey();

    /**
     * Gets the minimum position of the bounds.
     *
     * @return the minimum position of the bounds
     * @since 0.2.0
     */
    @Contract(value = " -> new", pure = true)
    BlockPosition minPosition();

    /**
     * Gets the maximum position of the bounds.
     *
     * @return the maximum position of the bounds
     * @since 0.2.0
     */
    @Contract(value = " -> new", pure = true)
    BlockPosition maxPosition();

    /**
     * Gets the minimum X coordinate of the bounds.
     *
     * @return the minimum X coordinate of the bounds
     * @since 0.2.0
     */
    int minX();

    /**
     * Gets the minimum Y coordinate of the bounds.
     *
     * @return the minimum Y coordinate of the bounds
     * @since 0.2.0
     */
    int minY();

    /**
     * Gets the minimum Z coordinate of the bounds.
     *
     * @return the minimum Z coordinate of the bounds
     * @since 0.2.0
     */
    int minZ();

    /**
     * Gets the maximum X coordinate of the bounds.
     *
     * @return the maximum X coordinate of the bounds
     * @since 0.2.0
     */
    int maxX();

    /**
     * Gets the maximum Y coordinate of the bounds.
     *
     * @return the maximum Y coordinate of the bounds
     * @since 0.2.0
     */
    int maxY();

    /**
     * Gets the maximum Z coordinate of the bounds.
     *
     * @return the maximum Z coordinate of the bounds
     * @since 0.2.0
     */
    int maxZ();

    /**
     * Searches for a safe location within the bounds using a smart algorithm.
     * <p>
     * The algorithm works as follows:
     * <ol>
     *   <li>Pick a random X and Z coordinate within the bounds</li>
     *   <li>Pick a random Y coordinate and search up and down for a safe location</li>
     *   <li>If no safe location is found within height bounds, try a new X coordinate</li>
     *   <li>If still no safe location is found, try a new Z coordinate</li>
     *   <li>If still no safe location is found, try both new X and Z coordinates</li>
     * </ol>
     *
     * @param random The random number generator.
     * @return A CompletableFuture that completes with a safe location, or {@code null} if none is found.
     * @since 0.2.0
     */
    CompletableFuture<@Nullable Location> searchSafeLocation(Random random);
}
