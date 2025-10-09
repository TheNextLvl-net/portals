package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
record SimpleCuboid(World world, Position min, Position max) implements Cuboid {
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
