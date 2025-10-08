package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
public interface BoundingBox {
    @Contract(pure = true)
    World getWorld();

    @Contract(pure = true)
    Position getMinPosition();

    @Contract(pure = true)
    Position getMaxPosition();

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
    boolean contains(Position position);

    @Contract(pure = true)
    default boolean contains(Entity entity) {
        return entity.getWorld().equals(getWorld()) && contains(entity.getLocation());
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static BoundingBox cuboid(World world, Position min, Position max) {
        return new CuboidBoundingBox(world, min, max);
    }
}
