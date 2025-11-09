package net.thenextlvl.portals.bounds;

import io.papermc.paper.math.Position;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Factory for creating Bounds instances.
 *
 * @since 0.2.0
 */
@ApiStatus.NonExtendable
public interface BoundsFactory {
    /**
     * Creates new Bounds for teleportation to happen within.
     *
     * @param world The key of the world the teleport will happen in.
     * @param minX  The minimum X coordinate.
     * @param minY  The minimum Y coordinate.
     * @param minZ  The minimum Z coordinate.
     * @param maxX  The maximum X coordinate.
     * @param maxY  The maximum Y coordinate.
     * @param maxZ  The maximum Z coordinate.
     * @since 0.2.0
     */
    @Contract(value = "_, _, _, _, _, _, _ -> new", pure = true)
    Bounds of(Key world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ);

    /**
     * Creates new Bounds for teleportation to happen within.
     *
     * @param world The world the teleport will happen in.
     * @param minX  The minimum X coordinate.
     * @param minY  The minimum Y coordinate.
     * @param minZ  The minimum Z coordinate.
     * @param maxX  The maximum X coordinate.
     * @param maxY  The maximum Y coordinate.
     * @param maxZ  The maximum Z coordinate.
     * @since 0.2.0
     */
    @Contract(value = "_, _, _, _, _, _, _ -> new", pure = true)
    Bounds of(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ);

    /**
     * Creates new Bounds for teleportation to happen within.
     *
     * @param key The world key.
     * @param min The minimum position.
     * @param max The maximum position.
     * @since 0.2.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    Bounds of(Key key, Position min, Position max);

    /**
     * Creates new Bounds for teleportation to happen within.
     *
     * @param world The world.
     * @param min   The minimum position.
     * @param max   The maximum position.
     * @since 0.2.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    Bounds of(World world, Position min, Position max);

    /**
     * Creates new Bounds based on a center position, radius, and height.
     *
     * @param world  The key of the world.
     * @param center The center position.
     * @param radius The radius.
     * @param height The height.
     * @since 0.2.0
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    Bounds radius(Key world, Position center, int radius, int height);

    /**
     * Creates new Bounds based on a center position, radius, and height.
     *
     * @param world  The world.
     * @param center The center position.
     * @param radius The radius.
     * @param height The height.
     * @since 0.2.0
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    Bounds radius(World world, Position center, int radius, int height);
}
