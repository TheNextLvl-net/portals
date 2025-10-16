package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.World;

record SimpleEllipsoid(World world, Position center, double radius, double height) implements Ellipsoid {
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
    // todo: check if this is correct, it looks wrong... it is midnight...
    public boolean contains(Position position) {
        var dx = position.x() - center.x();
        var dy = position.y() - center.y();
        var dz = position.z() - center.z();

        var distance = (dx * dx) + (dy / 2) * (dy / 2) + (dz * dz);
        return distance * distance < radius * radius;
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
