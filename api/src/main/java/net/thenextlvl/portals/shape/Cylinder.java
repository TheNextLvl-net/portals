package net.thenextlvl.portals.shape;

import org.jetbrains.annotations.Contract;

/**
 * Represents a cylinder in a {@link org.bukkit.World}.
 *
 * @since 0.1.0
 */
public sealed interface Cylinder extends Shape permits SimpleCylinder {
    @Contract(pure = true)
    double getRadius();

    @Contract(pure = true)
    double getHeight();
}
