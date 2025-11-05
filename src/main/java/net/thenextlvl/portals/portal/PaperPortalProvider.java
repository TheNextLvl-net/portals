package net.thenextlvl.portals.portal;

import com.google.common.base.Preconditions;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalProvider;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

@NullMarked
public final class PaperPortalProvider implements PortalProvider {
    public final Set<Portal> portals = new HashSet<>();
    private final PortalsPlugin plugin;

    public PaperPortalProvider(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Path getDataFolder(World world) {
        return world.getWorldPath().resolve("portals");
    }

    @Override
    public Optional<Portal> getPortal(String name) {
        return getPortals().filter(portal -> portal.getName().equals(name)).findAny();
    }

    @Override
    public Stream<Portal> getPortals() {
        return portals.stream();
    }

    @Override
    public Stream<Portal> getPortals(World world) {
        return getPortals().filter(portal -> portal.getBoundingBox().getWorld().equals(world));
    }

    @Override
    public Portal createPortal(String name, BoundingBox boundingBox) throws IllegalArgumentException {
        Preconditions.checkArgument(!hasPortal(name), "Portal with name '%s' already exists", name);
        var portal = new PaperPortal(plugin, name, boundingBox);
        portals.add(portal);
        return portal;
    }

    @Override
    public boolean hasPortal(Portal portal) {
        return portals.contains(portal);
    }

    @Override
    public boolean hasPortal(String name) {
        return getPortals().anyMatch(portal -> portal.getName().equals(name));
    }

    @Override
    public boolean deletePortal(Portal portal) {
        try {
            if (!portals.remove(portal)) return false;
            Files.deleteIfExists(portal.getDataFile());
            Files.deleteIfExists(portal.getBackupFile());
            return true;
        } catch (IOException e) {
            plugin.getComponentLogger().error("Failed to delete portal {}", portal.getName(), e);
            return false;
        }
    }

    @Override
    public boolean deletePortal(String name) {
        return getPortal(name).map(this::deletePortal).orElse(false);
    }

    @Override
    public void forEachPortal(Consumer<Portal> action) {
        portals.forEach(action);
    }
}
