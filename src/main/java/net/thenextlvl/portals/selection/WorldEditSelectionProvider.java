package net.thenextlvl.portals.selection;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.CylinderRegionSelector;
import com.sk89q.worldedit.regions.selector.EllipsoidRegionSelector;
import io.papermc.paper.math.Position;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.shape.BoundingBox;
import net.thenextlvl.portals.shape.Cuboid;
import net.thenextlvl.portals.shape.Cylinder;
import net.thenextlvl.portals.shape.Ellipsoid;
import net.thenextlvl.portals.shape.Shape;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
public final class WorldEditSelectionProvider implements SelectionProvider {
    private final PortalsPlugin plugin;
    private final WorldEdit worldEdit;

    public WorldEditSelectionProvider(PortalsPlugin plugin, WorldEdit worldEdit) {
        this.plugin = plugin;
        this.worldEdit = worldEdit;
    }

    @Override
    public Optional<Shape> getSelection(Player player) {
        var manager = worldEdit.getSessionManager();
        var owner = new BukkitPlayer(player);
        var session = manager.getIfPresent(owner);
        if (session == null) return Optional.empty();
        try {
            return Optional.ofNullable(fromRegion(session.getSelection()));
        } catch (IllegalArgumentException e) {
            plugin.getComponentLogger().error("Failed to convert WorldEdit region to shape", e);
            return Optional.empty();
        } catch (IncompleteRegionException e) {
            return Optional.empty();
        }
    }

    private @Nullable Shape fromRegion(Region region) {
        if (!(region.getWorld() instanceof BukkitWorld bukkit)) return null;
        var world = bukkit.getWorld();
        return switch (region) {
            case EllipsoidRegion ellipsoid ->
                    BoundingBox.ellipsoid(world, toPosition(ellipsoid.getCenter()), ellipsoid.getWidth(), ellipsoid.getHeight());
            case CylinderRegion cylinder ->
                    BoundingBox.cylinder(world, toPosition(cylinder.getCenter()), cylinder.getWidth(), cylinder.getHeight());
            case CuboidRegion cuboid ->
                    BoundingBox.cuboid(world, toPosition(cuboid.getPos1()), toPosition(cuboid.getPos2()));
            default -> throw new IllegalArgumentException("Unsupported region type: " + region.getClass().getName());
        };
    }

    @Override
    public boolean clearSelection(Player player) {
        var manager = worldEdit.getSessionManager();
        var owner = new BukkitPlayer(player);
        if (!manager.contains(owner)) return false;
        manager.remove(owner);
        return true;
    }

    @Override
    public boolean hasSelection(Player player) {
        return worldEdit.getSessionManager().contains(new BukkitPlayer(player));
    }

    private Position toPosition(BlockVector3 vector) {
        return Position.block(vector.x(), vector.y(), vector.z());
    }

    private Position toPosition(Vector3 vector) {
        return Position.fine(vector.x(), vector.y(), vector.z());
    }

    private BlockVector3 toBlockVector(Position position) {
        return BlockVector3.at(position.blockX(), position.blockY(), position.blockZ());
    }

    private Vector3 toVector(Position position) {
        return Vector3.at(position.x(), position.y(), position.z());
    }

    @Override
    public void setSelection(Player player, Shape shape) {
        var manager = worldEdit.getSessionManager();
        var session = manager.get(new BukkitPlayer(player));
        session.setRegionSelector(new BukkitWorld(shape.getWorld()), toRegionSelector(shape));
    }

    private RegionSelector toRegionSelector(Shape shape) {
        return switch (shape) {
            case Ellipsoid ellipsoid -> new EllipsoidRegionSelector(
                    new BukkitWorld(ellipsoid.getWorld()),
                    toBlockVector(ellipsoid.getCenter()),
                    new Vector3(ellipsoid.getRadius(), ellipsoid.getHeight(), ellipsoid.getRadius())
            );
            case Cylinder cylinder -> {
                var world = new BukkitWorld(cylinder.getWorld());
                var region = new CylinderRegion(world);

                var pos1 = toBlockVector(shape.getMinPosition());
                var pos2 = toBlockVector(shape.getMaxPosition());

                var center = pos1.add(pos2).divide(2).floor();
                var center2 = center.toBlockVector2();
                var radius = pos2.toBlockVector2().subtract(center2).toVector2();

                region.setCenter(center2);
                region.setRadius(radius);

                region.setMaximumY(Math.max(pos1.y(), pos2.y()));
                region.setMinimumY(Math.min(pos1.y(), pos2.y()));

                yield new CylinderRegionSelector(world, center2, radius, region.getMinimumY(), region.getMaximumY());
            }
            case Cuboid cuboid -> new CuboidRegionSelector(
                    new BukkitWorld(cuboid.getWorld()),
                    toBlockVector(cuboid.getMinPosition()),
                    toBlockVector(cuboid.getMaxPosition())
            );
            default -> throw new IllegalArgumentException("Unsupported shape type: " + shape.getClass().getName());
        };
    }
}
