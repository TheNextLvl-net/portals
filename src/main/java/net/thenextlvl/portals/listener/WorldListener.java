package net.thenextlvl.portals.listener;

import net.thenextlvl.nbt.NBTInputStream;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
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

import static net.thenextlvl.portals.PortalsPlugin.ISSUES;

@NullMarked
public final class WorldListener implements Listener {
    private final PortalsPlugin plugin;

    public WorldListener(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        var dataFolder = plugin.portalProvider().getDataFolder(event.getWorld());
        if (!Files.isDirectory(dataFolder)) return;
        try (var files = Files.find(dataFolder, 1, (path, attributes) -> {
            return attributes.isRegularFile() && path.getFileName().toString().endsWith(".dat");
        })) {
            files.forEach(path -> loadSafe(path, event.getWorld()));
        } catch (IOException e) {
            plugin.getComponentLogger().error("Failed to load all portals in world {}", event.getWorld().getName(), e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldSave(WorldSaveEvent event) {
        plugin.portalProvider().getPortals(event.getWorld()).forEach(Portal::persist);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        plugin.portalProvider().portals.removeIf(portal -> {
            if (portal.getWorld().equals(event.getWorld())) {
                portal.persist();
                return true;
            } else return false;
        });
    }

    private @Nullable Portal loadSafe(Path file, World world) {
        try {
            try (var inputStream = NBTInputStream.create(file)) {
                return load(inputStream, world);
            } catch (Exception e) {
                var backup = file.resolveSibling(file.getFileName() + "_old");
                if (!Files.isRegularFile(backup)) throw e;
                plugin.getComponentLogger().warn("Failed to load portal from {}", file, e);
                plugin.getComponentLogger().warn("Falling back to {}", backup);
                try (var inputStream = NBTInputStream.create(backup)) {
                    return load(inputStream, world);
                }
            }
        } catch (ParserException e) {
            plugin.getComponentLogger().warn("Failed to load portal from {}: {}", file, e.getMessage());
            return null;
        } catch (EOFException e) {
            plugin.getComponentLogger().error("The portal file {} is irrecoverably broken", file);
            return null;
        } catch (Exception e) {
            plugin.getComponentLogger().error("Failed to load portal from {}", file, e);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            return null;
        }
    }

    private @Nullable Portal load(NBTInputStream inputStream, World world) throws IOException {
        var portal = plugin.nbt(world).deserialize(inputStream.readTag(), Portal.class);
        if (plugin.portalProvider().portals.add(portal)) return portal;
        plugin.getComponentLogger().warn("A portal with the name '{}' is already loaded", portal.getName());
        return null;
    }
}
