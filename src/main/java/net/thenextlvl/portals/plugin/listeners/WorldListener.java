package net.thenextlvl.portals.plugin.listeners;

import net.thenextlvl.nbt.NBTInputStream;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class WorldListener implements Listener {
    private final PortalsPlugin plugin;

    public WorldListener(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(final WorldLoadEvent event) {
        final var dataFolder = plugin.portalProvider().getDataFolder(event.getWorld());
        if (!Files.isDirectory(dataFolder)) return;
        try (final var files = Files.find(dataFolder, 1, (path, attributes) -> {
            return attributes.isRegularFile() && path.getFileName().toString().endsWith(".dat");
        })) {
            files.forEach(path -> loadSafe(path, event.getWorld()));
        } catch (final IOException e) {
            plugin.getComponentLogger().error("Failed to load all portals in world {}", event.getWorld().getName(), e);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PortalsPlugin.ERROR_TRACKER.trackError(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldSave(final WorldSaveEvent event) {
        plugin.portalProvider().getPortals(event.getWorld()).forEach(Portal::persist);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(final WorldUnloadEvent event) {
        plugin.portalProvider().portals.removeIf(portal -> {
            if (portal.getWorld().equals(event.getWorld())) {
                portal.persist();
                return true;
            } else return false;
        });
    }

    private @Nullable Portal loadSafe(final Path file, final World world) {
        try {
            try (final var inputStream = NBTInputStream.create(file)) {
                return load(inputStream, world);
            } catch (final Exception e) {
                final var backup = file.resolveSibling(file.getFileName() + "_old");
                if (!Files.isRegularFile(backup)) throw e;
                plugin.getComponentLogger().warn("Failed to load portal from {}", file, e);
                plugin.getComponentLogger().warn("Falling back to {}", backup);
                try (final var inputStream = NBTInputStream.create(backup)) {
                    return load(inputStream, world);
                }
            }
        } catch (final ParserException e) {
            plugin.getComponentLogger().warn("Failed to load portal from {}: {}", file, e.getMessage());
            return null;
        } catch (final EOFException e) {
            plugin.getComponentLogger().error("The portal file {} is irrecoverably broken", file);
            return null;
        } catch (final Exception e) {
            plugin.getComponentLogger().error("Failed to load portal from {}", file, e);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PortalsPlugin.ERROR_TRACKER.trackError(e);
            return null;
        }
    }

    private @Nullable Portal load(final NBTInputStream inputStream, final World world) throws IOException {
        final var portal = plugin.nbt(world).deserialize(inputStream.readTag(), Portal.class);
        if (plugin.portalProvider().portals.add(portal)) return portal;
        plugin.getComponentLogger().warn("A portal with the name '{}' is already loaded", portal.getName());
        return null;
    }
}
