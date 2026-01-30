package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * Represents an AABB (Axis-Aligned Bounding Box) in a {@link World}.
 *
 * @since 0.1.0
 */
public sealed interface BoundingBox permits SimpleBoundingBox {
    /**
     * Gets the world of the bounding box.
     *
     * @return the world
     * @since 0.1.0
     */
    @Contract(pure = true)
    World getWorld();

    /**
     * Gets the minimum position of the bounding box.
     *
     * @return the minimum position
     * @since 0.1.0
     */
    @Contract(pure = true)
    Position getMinPosition();

    /**
     * Gets the maximum position of the bounding box.
     *
     * @return the maximum position
     * @since 0.1.0
     */
    @Contract(pure = true)
    Position getMaxPosition();

    /**
     * Gets the center of the bounding box.
     *
     * @return the center
     * @since 0.1.0
     */
    @Contract(value = " -> new", pure = true)
    Location getCenter();

    /**
     * Gets the minimum location of the bounding box.
     *
     * @return the minimum location
     * @since 0.1.0
     */
    @Contract(value = " -> new", pure = true)
    Location getMinLocation();

    /**
     * Gets the maximum location of the bounding box.
     *
     * @return the maximum location
     * @since 0.1.0
     */
    @Contract(value = " -> new", pure = true)
    Location getMaxLocation();

    /**
     * Gets the minimum X coordinate of the bounding box.
     *
     * @return the minimum X coordinate
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getMinX();

    /**
     * Gets the minimum Y coordinate of the bounding box.
     *
     * @return the minimum Y coordinate
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getMinY();

    /**
     * Gets the minimum Z coordinate of the bounding box.
     *
     * @return the minimum Z coordinate
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getMinZ();

    /**
     * Gets the maximum X coordinate of the bounding box.
     *
     * @return the maximum X coordinate
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getMaxX();

    /**
     * Gets the maximum Y coordinate of the bounding box.
     *
     * @return the maximum Y coordinate
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getMaxY();

    /**
     * Gets the maximum Z coordinate of the bounding box.
     *
     * @return the maximum Z coordinate
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getMaxZ();

    /**
     * Checks if the bounding box contains the given position.
     *
     * @param position the position to check
     * @return {@code true} if the bounding box contains the position
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean contains(Position position);

    /**
     * Checks if the bounding box contains the given entity.
     *
     * @param entity the entity to check
     * @return {@code true} if the bounding box contains the entity
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean contains(Entity entity);

    /**
     * Checks if the bounding box intersects with another bounding box.
     *
     * @param boundingBox the bounding box to check
     * @return {@code true} if the bounding boxes intersect
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean intersects(BoundingBox boundingBox);

    /**
     * Checks if the bounding box overlaps with another bounding box.
     *
     * @param boundingBox the bounding box to check
     * @return {@code true} if the bounding boxes overlap
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean overlaps(org.bukkit.util.BoundingBox boundingBox);

    /**
     * Creates a new bounding box.
     *
     * @param world the world
     * @param min   the minimum position of the bounding box
     * @param max   the maximum position of the bounding box
     * @return a new bounding box
     * @since 0.1.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static BoundingBox of(final World world, final Position min, final Position max) {
        return new SimpleBoundingBox(world, min, max);
    }
}
