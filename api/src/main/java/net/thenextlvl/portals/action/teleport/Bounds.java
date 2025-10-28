package net.thenextlvl.portals.action.teleport;

import io.papermc.paper.math.Position;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Bounds(
        double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ
) {
    /**
     * Creates new Bounds for teleportation to happen within.
     *
     * @param minX The minimum X coordinate.
     * @param minY The minimum Y coordinate.
     * @param minZ The minimum Z coordinate.
     * @param maxX The maximum X coordinate.
     * @param maxY The maximum Y coordinate.
     * @param maxZ The maximum Z coordinate.
     * @since 0.1.0
     */
    public Bounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
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
     * @since 0.1.0
     */
    public Bounds(Position min, Position max) {
        this(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }

    /**
     * Creates new Teleportation Bounds based on a center position, radius, and height.
     *
     * @param center The center position.
     * @param radius The radius.
     * @param height The height.
     * @since 0.1.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Bounds radius(Position center, double radius, double height) {
        return new Bounds(
                center.x() - radius,
                center.y() - height / 2,
                center.z() - radius,
                center.x() + radius,
                center.y() + height / 2,
                center.z() + radius
        );
    }
}
