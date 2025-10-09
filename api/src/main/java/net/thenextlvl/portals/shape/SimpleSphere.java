package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
record SimpleSphere(World world, Position center, double radius) implements Sphere {
    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Position getMinPosition() {
        return center.offset(-radius, -radius, -radius);
    }

    @Override
    public Position getMaxPosition() {
        return center.offset(radius, radius, radius);
    }

    @Override
    public double getMinX() {
        return center.x() - radius;
    }

    @Override
    public double getMinY() {
        return center.y() - radius;
    }

    @Override
    public double getMinZ() {
        return center.z() - radius;
    }

    @Override
    public double getMaxX() {
        return center.x() + radius;
    }

    @Override
    public double getMaxY() {
        return center.y() + radius;
    }

    @Override
    public double getMaxZ() {
        return center.z() + radius;
    }

    @Override
    public boolean contains(Position position) {
        var distance = (position.x() - center.x()) * (position.x() - center.x()) +
                (position.y() - center.y()) * (position.y() - center.y()) +
                (position.z() - center.z()) * (position.z() - center.z());
        return distance * 2 < radius * radius;
    }

    @Override
    public double getRadius() {
        return radius;
    }
}
