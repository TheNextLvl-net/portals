package net.thenextlvl.portals.bounds;

import io.papermc.paper.math.Position;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleBoundsFactory implements BoundsFactory {
    public static final SimpleBoundsFactory INSTANCE = new SimpleBoundsFactory();

    @Override
    public Bounds of(Key world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new SimpleBounds(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public Bounds of(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return of(world.key(), minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public Bounds of(Key key, Position min, Position max) {
        return of(key, min.blockX(), min.blockY(), min.blockZ(), max.blockX(), max.blockY(), max.blockZ());
    }

    @Override
    public Bounds of(World world, Position min, Position max) {
        return of(world.key(), min, max);
    }

    @Override
    public Bounds radius(Key world, Position center, int radius, int height) {
        return of(world,
                center.blockX() - radius,
                center.blockY() - height / 2,
                center.blockZ() - radius,
                center.blockX() + radius,
                center.blockY() + height / 2,
                center.blockZ() + radius
        );
    }

    @Override
    public Bounds radius(World world, Position center, int radius, int height) {
        return radius(world.key(), center, radius, height);
    }
}
