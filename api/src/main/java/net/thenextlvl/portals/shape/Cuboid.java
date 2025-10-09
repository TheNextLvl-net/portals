package net.thenextlvl.portals.shape;

import org.jspecify.annotations.NullMarked;

/**
 * Represents a cuboid in a {@link org.bukkit.World}.
 *
 * @since 0.1.0
 */
@NullMarked
public sealed interface Cuboid extends BoundingBox permits SimpleCuboid {
}
