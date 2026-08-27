package net.thenextlvl.portals.plugin.effects;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class PortalEffectScheduler {
    private static final double VIEW_DISTANCE_SQUARED = 32 * 32;

    private final Map<Key, Long> animationTicks = new ConcurrentHashMap<>();
    private final PortalsPlugin plugin;

    private @Nullable ScheduledTask task;

    public PortalEffectScheduler(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (task != null && !task.isCancelled()) return;
        task = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, ignored -> {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.getScheduler().run(plugin, task -> playNearby(player), null);
            });
        }, 1, 1);
    }

    public void stop() {
        if (task == null) return;
        task.cancel();
        task = null;
        animationTicks.clear();
    }

    private void playNearby(final Player player) {
        if (!player.isOnline()) return;
        final var location = player.getLocation();
        plugin.portalProvider().getPortals(player.getWorld())
                .filter(portal -> portal.getBoundingBox().getCenter().distanceSquared(location) <= VIEW_DISTANCE_SQUARED)
                .forEach(portal -> portal.getPortalEffect().ifPresent(effect -> play(player, portal, effect)));
    }

    private void play(final Player player, final Portal portal, final PortalEffect effect) {
        final var key = new Key(player.getUniqueId(), portal.getName());
        final var animationTick = animationTicks.merge(key, 1L, Long::sum);
        final var bounds = portal.getBoundingBox();
        final var xLength = bounds.getMaxX() - bounds.getMinX();
        final var yLength = bounds.getMaxY() - bounds.getMinY();
        final var zLength = bounds.getMaxZ() - bounds.getMinZ();
        final var width = Math.max(xLength, zLength);
        final var origin = new PortalEffectOrigin(bounds.getCenter().subtract(0, 0.5, 0), width, yLength, animationTick);
        origin.setYaw(xLength >= zLength ? 0f : 90f);
        effect.play(player, origin);
    }

    private record Key(UUID player, String portal) {
    }
}
