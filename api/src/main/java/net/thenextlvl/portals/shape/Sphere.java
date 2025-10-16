package net.thenextlvl.portals.shape;

import org.jetbrains.annotations.Contract;

/**
 * Represents a sphere in a {@link org.bukkit.World}.
 *
 * @since 0.1.0
 */
public sealed interface Sphere extends Shape permits SimpleSphere {
    @Contract(pure = true)
    double getRadius();
}
