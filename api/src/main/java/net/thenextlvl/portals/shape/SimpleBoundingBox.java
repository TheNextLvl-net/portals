package net.thenextlvl.portals.shape;

import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

record SimpleBoundingBox(World world, Position minPosition, Position maxPosition) implements BoundingBox {

    SimpleBoundingBox(World world, Position minPosition, Position maxPosition) {
        this.world = world;
        var minX = Math.min(minPosition.x(), maxPosition.x());
        var minY = Math.min(minPosition.y(), maxPosition.y());
        var minZ = Math.min(minPosition.z(), maxPosition.z());
        this.minPosition = Position.fine(minX, minY, minZ);
        var maxX = Math.max(minPosition.x(), maxPosition.x());
        var maxY = Math.max(minPosition.y(), maxPosition.y());
        var maxZ = Math.max(minPosition.z(), maxPosition.z());
        this.maxPosition = Position.fine(maxX, maxY, maxZ);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Position getMinPosition() {
        return minPosition;
    }

    @Override
    public Position getMaxPosition() {
        return maxPosition;
    }

    @Override
    public Location getCenter() {
        return new Location(world, (getMinX() + getMaxX()) / 2, (getMinY() + getMaxY()) / 2, (getMinZ() + getMaxZ()) / 2);
    }

    @Override
    public Location getMinLocation() {
        return minPosition.toLocation(world);
    }

    @Override
    public Location getMaxLocation() {
        return maxPosition.toLocation(world);
    }

    @Override
    public double getMinX() {
        return minPosition.x();
    }

    @Override
    public double getMinY() {
        return minPosition.y();
    }

    @Override
    public double getMinZ() {
        return minPosition.z();
    }

    @Override
    public double getMaxX() {
        return maxPosition.x();
    }

    @Override
    public double getMaxY() {
        return maxPosition.y();
    }

    @Override
    public double getMaxZ() {
        return maxPosition.z();
    }

    @Override
    public boolean contains(Position position) {
        return position.x() >= getMinX() && position.x() <= getMaxX() &&
                position.y() >= getMinY() && position.y() <= getMaxY() &&
                position.z() >= getMinZ() && position.z() <= getMaxZ();
    }

    @Override
    public boolean contains(Entity entity) {
        return entity.getWorld().equals(world) && overlaps(entity.getBoundingBox());
    }

    @Override
    public boolean intersects(BoundingBox boundingBox) {
        return world.equals(boundingBox.getWorld()) &&
                getMinX() <= boundingBox.getMaxX() &&
                getMaxX() >= boundingBox.getMinX() &&
                getMinY() <= boundingBox.getMaxY() &&
                getMaxY() >= boundingBox.getMinY() &&
                getMinZ() <= boundingBox.getMaxZ() &&
                getMaxZ() >= boundingBox.getMinZ();
    }

    @Override
    public boolean overlaps(org.bukkit.util.BoundingBox boundingBox) {
        return boundingBox.overlaps(minPosition().toVector(), maxPosition().toVector());
    }
}
