package net.thenextlvl.portals.plugin.selections;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import net.thenextlvl.portals.selection.SelectionProvider;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
public final class WorldEditSelectionProvider implements SelectionProvider {
    private final WorldEdit worldEdit = WorldEdit.getInstance();

    @Override
    public Optional<BoundingBox> getSelection(Player player) {
        var manager = worldEdit.getSessionManager();
        var owner = BukkitAdapter.adapt(player);
        var session = manager.getIfPresent(owner);
        if (session == null) return Optional.empty();
        try {
            if (!(session.getSelection() instanceof CuboidRegion cuboid)) return Optional.empty();
            var world = cuboid.getWorld() instanceof BukkitWorld bukkit ? bukkit.getWorld() : player.getWorld();
            var min = toPosition(cuboid.getMinimumPoint());
            var max = toPosition(cuboid.getMaximumPoint()).offset(1, 1, 1);
            return Optional.of(BoundingBox.of(world, min, max));
        } catch (IncompleteRegionException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean clearSelection(Player player) {
        var manager = worldEdit.getSessionManager();
        var owner = BukkitAdapter.adapt(player);
        if (!manager.contains(owner)) return false;
        manager.remove(owner);
        return true;
    }

    @Override
    public boolean hasSelection(Player player) {
        return getSelection(player).isPresent();
    }

    @Override
    public void setSelection(Player player, BoundingBox boundingBox) {
        var manager = worldEdit.getSessionManager();
        var session = manager.get(BukkitAdapter.adapt(player));
        var selector = new CuboidRegionSelector(
                BukkitAdapter.adapt(boundingBox.getWorld()),
                toBlockVector(boundingBox.getMinPosition()),
                toBlockVector(boundingBox.getMaxPosition())
        );
        session.setRegionSelector(BukkitAdapter.adapt(boundingBox.getWorld()), selector);
    }

    private BlockPosition toPosition(BlockVector3 vector) {
        return Position.block(vector.x(), vector.y(), vector.z());
    }

    private BlockVector3 toBlockVector(Position position) {
        return BlockVector3.at(position.blockX(), position.blockY(), position.blockZ());
    }
}
