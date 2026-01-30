package net.thenextlvl.portals.plugin.bounds;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.thenextlvl.portals.bounds.Bounds;
import net.thenextlvl.portals.plugin.PortalsPlugin;
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

    public SimpleBounds(final Key worldKey, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
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
    public CompletableFuture<@Nullable Location> searchSafeLocation(final Random random) {
        final var world = world().orElse(null);
        if (world == null) return CompletableFuture.completedFuture(null);

        // Clamp bounds to world border
        final var border = world.getWorldBorder();
        final var borderSize = Math.min((int) border.getSize() / 2, plugin.getServer().getMaxWorldSize());
        final var centerX = border.getCenter().getBlockX();
        final var centerZ = border.getCenter().getBlockZ();
        final var borderMinX = centerX - borderSize;
        final var borderMaxX = centerX + borderSize;
        final var borderMinZ = centerZ - borderSize;
        final var borderMaxZ = centerZ + borderSize;

        final var clampedMinX = Math.clamp(minX, borderMinX, borderMaxX);
        final var clampedMaxX = Math.clamp(maxX, borderMinX, borderMaxX);
        final var clampedMinZ = Math.clamp(minZ, borderMinZ, borderMaxZ);
        final var clampedMaxZ = Math.clamp(maxZ, borderMinZ, borderMaxZ);

        final var initialX = clampedMinX == clampedMaxX ? clampedMaxX : random.nextInt(clampedMinX, clampedMaxX);
        final var initialZ = clampedMinZ == clampedMaxZ ? clampedMaxZ : random.nextInt(clampedMinZ, clampedMaxZ);

        // Try initial X and Z position
        return searchSafeLocationAtXZ(random, world, initialX, initialZ).thenCompose(location -> {
            if (location != null) return CompletableFuture.completedFuture(location);

            // Try different X position
            final var newX = getAlternativeCoordinate(random, initialX, clampedMinX, clampedMaxX);
            return searchSafeLocationAtXZ(random, world, newX, initialZ);
        }).thenCompose(location -> {
            if (location != null) return CompletableFuture.completedFuture(location);

            // Try different Z position
            final var newZ = getAlternativeCoordinate(random, initialZ, clampedMinZ, clampedMaxZ);
            return searchSafeLocationAtXZ(random, world, initialX, newZ);
        }).thenCompose(location -> {
            if (location != null) return CompletableFuture.completedFuture(location);

            // Try both new X and Z
            final var newX = getAlternativeCoordinate(random, initialX, clampedMinX, clampedMaxX);
            final var newZ = getAlternativeCoordinate(random, initialZ, clampedMinZ, clampedMaxZ);
            return searchSafeLocationAtXZ(random, world, newX, newZ);
        });
    }

    private CompletableFuture<@Nullable Location> searchSafeLocationAtXZ(final Random random, final World world, final int x, final int z) {
        // Clamp to world height limits
        final var minY = Math.max(this.minY, world.getMinHeight());
        final var maxY = Math.min(this.maxY, world.getLogicalHeight());

        final var startY = minY == maxY ? maxY : random.nextInt(minY, maxY + 1);

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

    private int getAlternativeCoordinate(final Random random, final int current, final int min, final int max) {
        if (min == max) return max;
        return random.nextInt(min, max);
    }

    private boolean isSafeLocation(final World world, final int x, final int y, final int z) {
        if (!isValidSpawn(world.getBlockAt(x, y - 1, z))) return false;
        if (isInvalidSpawnInside(world.getBlockAt(x, y, z))) return false;
        return !isInvalidSpawnInside(world.getBlockAt(x, y + 1, z));
    }

    private boolean isValidSpawn(final Block block) {
        return block.isCollidable() && block.getType().isOccluding() && !block.getType().equals(Material.BEDROCK);
    }

    private boolean isInvalidSpawnInside(final Block block) {
        if (block.getLightFromSky() == 0 && !plugin.config().allowCaveSpawns()) return true;
        return !block.isPassable() || block.isLiquid() || block.getType().equals(Material.KELP);
    }

    private boolean isTagged(final Block block, final TagKey<BlockType> tag) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK)
                .getTagValues(tag).contains(block.getType().asBlockType());
    }
}
