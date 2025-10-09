package net.thenextlvl.portals.shape;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a sphere in a {@link org.bukkit.World}.
 *
 * @since 0.1.0
 */
@NullMarked
public sealed interface Sphere extends BoundingBox permits SimpleSphere {
    @Contract(pure = true)
    double getRadius();
}
