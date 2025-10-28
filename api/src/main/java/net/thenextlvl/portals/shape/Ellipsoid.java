package net.thenextlvl.portals.shape;

import org.jetbrains.annotations.Contract;

/**
 * Represents an ellipsoid in a {@link org.bukkit.World}.
 *
 * @since 0.1.0
 */
public sealed interface Ellipsoid extends Shape permits SimpleEllipsoid {
    @Contract(pure = true)
    double getRadius();

    @Contract(pure = true)
    double getHeight();
}
