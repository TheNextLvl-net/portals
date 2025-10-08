package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
record CuboidBoundingBox(World world, Position min, Position max) implements BoundingBox {
    @Override
    public boolean contains(Position position) {
        return position.x() >= min.x() && position.x() <= max.x() &&
                position.y() >= min.y() && position.y() <= max.y() &&
                position.z() >= min.z() && position.z() <= max.z();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Position getMinPosition() {
        return min;
    }

    @Override
    public Position getMaxPosition() {
        return max;
    }
}
