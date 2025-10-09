package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
record SimpleCylinder(World world, Position center, double radius, double height) implements Cylinder {
    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Position getMinPosition() {
        return center.offset(-radius, -height / 2, -radius);
    }

    @Override
    public Position getMaxPosition() {
        return center.offset(radius, height / 2, radius);
    }

    @Override
    public Position getCenter() {
        return center;
    }

    @Override
    public double getMinX() {
        return center.x() - radius;
    }

    @Override
    public double getMinY() {
        return center.y() - height / 2;
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
        return center.y() + height / 2;
    }

    @Override
    public double getMaxZ() {
        return center.z() + radius;
    }

    @Override
    public boolean contains(Position position) {
        return (center.y() + height / 2 >= position.y() && center.y() - height / 2 <= position.y())
                && (position.x() - center.x()) * (position.x() - center.x())
                + (position.z() - center.z()) * (position.z() - center.z()) <= radius * radius;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getHeight() {
        return height;
    }
}
