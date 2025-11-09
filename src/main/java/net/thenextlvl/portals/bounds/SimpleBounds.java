package net.thenextlvl.portals.bounds;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.thenextlvl.portals.PortalsPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@NullMarked
record SimpleBounds(
        Key worldKey,
        int minX, int minY, int minZ,
        int maxX, int maxY, int maxZ
) implements Bounds {
    private static final PortalsPlugin plugin = JavaPlugin.getPlugin(PortalsPlugin.class);

    public SimpleBounds(Key worldKey, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
        this.worldKey = worldKey;
    }

    @Override
    public Optional<World> world() {
        return Optional.ofNullable(plugin.getServer().getWorld(worldKey));
    }

    @Override
    public BlockPosition minPosition() {
        return Position.block(minX, minY, minZ);
    }

    @Override
    public BlockPosition maxPosition() {
        return Position.block(maxX, maxY, maxZ);
    }

    @Override
    public CompletableFuture<@Nullable Location> searchSafeLocation(Random random) {
        var world = world().orElse(null);
        if (world == null) return CompletableFuture.completedFuture(null);

        // Clamp bounds to world border
        var border = world.getWorldBorder();
        var borderSize = Math.min((int) border.getSize() / 2, plugin.getServer().getMaxWorldSize());
        var centerX = border.getCenter().getBlockX();
        var centerZ = border.getCenter().getBlockZ();
        var borderMinX = centerX - borderSize;
        var borderMaxX = centerX + borderSize;
        var borderMinZ = centerZ - borderSize;
        var borderMaxZ = centerZ + borderSize;

        var clampedMinX = Math.clamp(minX, borderMinX, borderMaxX);
        var clampedMaxX = Math.clamp(maxX, borderMinX, borderMaxX);
        var clampedMinZ = Math.clamp(minZ, borderMinZ, borderMaxZ);
        var clampedMaxZ = Math.clamp(maxZ, borderMinZ, borderMaxZ);

        var initialX = clampedMinX == clampedMaxX ? clampedMaxX : random.nextInt(clampedMinX, clampedMaxX);
        var initialZ = clampedMinZ == clampedMaxZ ? clampedMaxZ : random.nextInt(clampedMinZ, clampedMaxZ);

        // Try initial X and Z position
        return searchSafeLocationAtXZ(random, world, initialX, initialZ).thenCompose(location -> {
            if (location != null) return CompletableFuture.completedFuture(location);

            // Try different X position
            var newX = getAlternativeCoordinate(random, initialX, clampedMinX, clampedMaxX);
            return searchSafeLocationAtXZ(random, world, newX, initialZ);
        }).thenCompose(location -> {
            if (location != null) return CompletableFuture.completedFuture(location);

            // Try different Z position
            var newZ = getAlternativeCoordinate(random, initialZ, clampedMinZ, clampedMaxZ);
            return searchSafeLocationAtXZ(random, world, initialX, newZ);
        }).thenCompose(location -> {
            if (location != null) return CompletableFuture.completedFuture(location);

            // Try both new X and Z
            var newX = getAlternativeCoordinate(random, initialX, clampedMinX, clampedMaxX);
            var newZ = getAlternativeCoordinate(random, initialZ, clampedMinZ, clampedMaxZ);
            return searchSafeLocationAtXZ(random, world, newX, newZ);
        });
    }

    private CompletableFuture<@Nullable Location> searchSafeLocationAtXZ(Random random, World world, int x, int z) {
        // Clamp to world height limits
        var minY = Math.max(this.minY, world.getMinHeight());
        var maxY = Math.min(this.maxY, world.getLogicalHeight());

        var startY = minY == maxY ? maxY : random.nextInt(minY, maxY + 1);

        // Load chunk asynchronously before accessing blocks
        return world.getChunkAtAsync(x >> 4, z >> 4).thenApply(chunk -> {
            // Search upward from startY
            for (int y = startY; y <= maxY; y++) {
                if (isSafeLocation(world, x, y, z)) {
                    return new Location(world, x + 0.5, y, z + 0.5);
                }
            }

            // Search downward from startY
            for (int y = startY - 1; y >= minY; y--) {
                if (isSafeLocation(world, x, y, z)) {
                    return new Location(world, x + 0.5, y, z + 0.5);
                }
            }

            return null;
        });
    }

    private int getAlternativeCoordinate(Random random, int current, int min, int max) {
        if (min == max) return max;
        return random.nextInt(min, max);
    }

    private boolean isSafeLocation(World world, int x, int y, int z) {
        if (!isValidSpawn(world.getBlockAt(x, y - 1, z))) return false;
        if (isInvalidSpawnInside(world.getBlockAt(x, y, z))) return false;
        return !isInvalidSpawnInside(world.getBlockAt(x, y + 1, z));
    }

    private boolean isValidSpawn(Block block) {
        return block.isCollidable() && block.getType().isOccluding() && !block.getType().equals(Material.BEDROCK);
    }

    private boolean isInvalidSpawnInside(Block block) {
        if (block.getLightFromSky() == 0 && !plugin.config().allowCaveSpawns()) return true;
        return !block.isPassable() || block.isLiquid() || block.getType().equals(Material.KELP);
    }
    
    private boolean isTagged(Block block, TagKey<BlockType> tag) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK)
                .getTagValues(tag).contains(block.getType().asBlockType());
    }
}
