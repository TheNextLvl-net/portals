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
public interface BoundingBox {
    @Contract(pure = true)
    World getWorld();

    @Contract(pure = true)
    Position getMinPosition();

    @Contract(pure = true)
    Position getMaxPosition();

    @Contract(pure = true)
    default Position getCenter() {
        return Position.fine((getMinX() + getMaxX()) / 2, (getMinY() + getMaxY()) / 2, (getMinZ() + getMaxZ()) / 2);
    }

    @Contract(value = " -> new", pure = true)
    default Location getMinLocation() {
        return getMinPosition().toLocation(getWorld());
    }

    @Contract(value = " -> new", pure = true)
    default Location getMaxLocation() {
        return getMaxPosition().toLocation(getWorld());
    }

    @Contract(pure = true)
    default double getMinX() {
        return getMinPosition().x();
    }

    @Contract(pure = true)
    default double getMinY() {
        return getMinPosition().y();
    }

    @Contract(pure = true)
    default double getMinZ() {
        return getMinPosition().z();
    }

    @Contract(pure = true)
    default double getMaxX() {
        return getMaxPosition().x();
    }

    @Contract(pure = true)
    default double getMaxY() {
        return getMaxPosition().y();
    }

    @Contract(pure = true)
    default double getMaxZ() {
        return getMaxPosition().z();
    }

    @Contract(pure = true)
    default boolean contains(Position position) {
        return position.x() >= getMinX() && position.x() <= getMaxX() &&
                position.y() >= getMinY() && position.y() <= getMaxY() &&
                position.z() >= getMinZ() && position.z() <= getMaxZ();
    }

    @Contract(pure = true)
    default boolean contains(Entity entity) {
        return entity.getWorld().equals(getWorld()) && overlaps(entity.getBoundingBox());
    }

    @Contract(pure = true)
    default boolean intersects(BoundingBox boundingBox) {
        return getWorld().equals(boundingBox.getWorld()) &&
                getMinX() <= boundingBox.getMaxX() &&
                getMaxX() >= boundingBox.getMinX() &&
                getMinY() <= boundingBox.getMaxY() &&
                getMaxY() >= boundingBox.getMinY() &&
                getMinZ() <= boundingBox.getMaxZ() &&
                getMaxZ() >= boundingBox.getMinZ();
    }

    @Contract(pure = true)
    default boolean overlaps(org.bukkit.util.BoundingBox boundingBox) {
        return boundingBox.overlaps(getMinPosition().toVector(), getMaxPosition().toVector());
    }

    /**
     * Creates a new {@link Cuboid} bounding box.
     *
     * @param world the world
     * @param min   the minimum position of the cube
     * @param max   the maximum position of the cube
     * @return a new {@link Cuboid} bounding box
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static Cuboid cuboid(World world, Position min, Position max) {
        return new SimpleCuboid(world, min, max);
    }

    /**
     * Creates a new {@link Cylinder} bounding box.
     *
     * @param world  the world
     * @param center the center position of the cylinder
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     * @return a new {@link Cylinder} bounding box
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static Cylinder cylinder(World world, Position center, double radius, double height) {
        return new SimpleCylinder(world, center, radius, height);
    }

    /**
     * Creates a new {@link Ellipsoid} bounding box.
     *
     * @param world  the world
     * @param center the center position of the ellipsoid
     * @param radius the radius of the ellipsoid
     * @param height the height of the ellipsoid
     * @return a new {@link Ellipsoid} bounding box
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static Ellipsoid ellipsoid(World world, Position center, double radius, double height) {
        return new SimpleEllipsoid(world, center, radius, height);
    }

    /**
     * Creates a new {@link Sphere} bounding box.
     *
     * @param world  the world
     * @param center the center position of the sphere
     * @param radius the radius of the sphere
     * @return a new {@link Sphere} bounding box
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static Sphere sphere(World world, Position center, double radius) {
        return new SimpleSphere(world, center, radius);
    }
}
