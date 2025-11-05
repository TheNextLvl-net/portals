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
    @Contract(pure = true)
    World getWorld();

    @Contract(pure = true)
    Position getMinPosition();

    @Contract(pure = true)
    Position getMaxPosition();

    @Contract(value = " -> new", pure = true)
    Location getCenter();

    @Contract(value = " -> new", pure = true)
    Location getMinLocation();

    @Contract(value = " -> new", pure = true)
    Location getMaxLocation();

    @Contract(pure = true)
    double getMinX();

    @Contract(pure = true)
    double getMinY();

    @Contract(pure = true)
    double getMinZ();

    @Contract(pure = true)
    double getMaxX();

    @Contract(pure = true)
    double getMaxY();

    @Contract(pure = true)
    double getMaxZ();

    @Contract(pure = true)
    boolean contains(Position position);

    @Contract(pure = true)
    boolean contains(Entity entity);

    @Contract(pure = true)
    boolean intersects(BoundingBox boundingBox);

    @Contract(pure = true)
    boolean overlaps(org.bukkit.util.BoundingBox boundingBox);

    /**
     * Creates a new bounding box.
     *
     * @param world the world
     * @param min   the minimum position of the bounding box
     * @param max   the maximum position of the bounding box
     * @return a new bounding box
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static BoundingBox of(World world, Position min, Position max) {
        return new SimpleBoundingBox(world, min, max);
    }
}
