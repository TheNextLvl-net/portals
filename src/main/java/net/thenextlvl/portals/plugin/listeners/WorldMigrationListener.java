package net.thenextlvl.portals.plugin.listeners;

import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.worlds.event.WorldFolderMigrateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;

import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class WorldMigrationListener implements Listener {
    private final PortalsPlugin plugin;

    public WorldMigrationListener(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldFolderMigrate(final WorldFolderMigrateEvent event) {
        final var oldPath = event.getOldFolder().resolve("portals");
        if (!Files.isDirectory(oldPath)) return;

        final var dataFolder = event.getNewFolder().resolve("data");
        final var newPath = dataFolder.resolve("portals");
        try {
            Files.createDirectories(dataFolder);
            Files.move(oldPath, newPath);
            plugin.getComponentLogger().info("Migrated portals from {} to {}", oldPath, newPath);
        } catch (final IOException e) {
            plugin.getComponentLogger().error("Failed to migrate portals from {} to {}", oldPath, newPath, e);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PortalsPlugin.ERROR_TRACKER.trackError(e);
        }
    }
}
