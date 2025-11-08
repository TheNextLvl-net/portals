package net.thenextlvl.portals.model;

import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;

import java.util.Random;

/**
 * Represents a bounding box for teleportation.
 *
 * @param world The world the teleport will happen in.
 * @param minX  The minimum X coordinate.
 * @param minY  The minimum Y coordinate.
 * @param minZ  The minimum Z coordinate.
 * @param maxX  The maximum X coordinate.
 * @param maxY  The maximum Y coordinate.
 * @param maxZ  The maximum Z coordinate.
 */
public record Bounds(
        World world,
        double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ
) {
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
     */
    public Bounds(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.world = world;
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    /**
     * Creates new Bounds for teleportation to happen within.
     *
     * @param min The minimum position.
     * @param max The maximum position.
     */
    public Bounds(World world, Position min, Position max) {
        this(world, min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }

    /**
     * Gets the minimum position of the bounds.
     *
     * @return the minimum position of the bounds
     * @since 0.1.0
     */
    @Contract(value = " -> new", pure = true)
    public FinePosition minPosition() {
        return Position.fine(minX, minY, minZ);
    }

    /**
     * Gets the maximum position of the bounds.
     *
     * @return the maximum position of the bounds
     * @since 0.1.0
     */
    @Contract(value = " -> new", pure = true)
    public FinePosition maxPosition() {
        return Position.fine(maxX, maxY, maxZ);
    }

    /**
     * Creates new Teleportation Bounds based on a center position, radius, and height.
     *
     * @param center The center position.
     * @param radius The radius.
     * @param height The height.
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static Bounds radius(World world, Position center, double radius, double height) {
        return new Bounds(
                world,
                center.x() - radius,
                center.y() - height / 2,
                center.z() - radius,
                center.x() + radius,
                center.y() + height / 2,
                center.z() + radius
        );
    }

    /**
     * Gets a random location within the bounds.
     *
     * @param random The random number generator.
     * @return A random location within the bounds.
     */
    public Location getRandomLocation(Random random) {
        return new Location(
                world,
                minX == maxX ? maxX : random.nextDouble(minX, maxX),
                minY == maxY ? maxY : random.nextDouble(minY, maxY),
                minZ == maxZ ? maxZ : random.nextDouble(minZ, maxZ)
        );
    }
    // todo: safe teleportation – searchSafeLocation 
}
