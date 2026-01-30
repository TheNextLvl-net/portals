package net.thenextlvl.portals.plugin.bounds;

import io.papermc.paper.math.Position;
import net.kyori.adventure.key.Key;
import net.thenextlvl.portals.bounds.Bounds;
import net.thenextlvl.portals.bounds.BoundsFactory;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleBoundsFactory implements BoundsFactory {
    public static final SimpleBoundsFactory INSTANCE = new SimpleBoundsFactory();

    @Override
    public Bounds of(final Key world, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        return new SimpleBounds(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public Bounds of(final World world, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        return of(world.key(), minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public Bounds of(final Key key, final Position min, final Position max) {
        return of(key, min.blockX(), min.blockY(), min.blockZ(), max.blockX(), max.blockY(), max.blockZ());
    }

    @Override
    public Bounds of(final World world, final Position min, final Position max) {
        return of(world.key(), min, max);
    }

    @Override
    public Bounds radius(final Key world, final Position center, final int radius, final int height) {
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
    public Bounds radius(final World world, final Position center, final int radius, final int height) {
        return radius(world.key(), center, radius, height);
    }
}
