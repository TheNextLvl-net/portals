package net.thenextlvl.portals.shape;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a cylinder in a {@link org.bukkit.World}.
 *
 * @since 0.1.0
 */
@NullMarked
public sealed interface Cylinder extends BoundingBox permits SimpleCylinder {
    @Contract(pure = true)
    double getRadius();

    @Contract(pure = true)
    double getHeight();
}
