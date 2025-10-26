package net.thenextlvl.portals.portal;

import com.google.common.base.Preconditions;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalProvider;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@NullMarked
public final class PaperPortalProvider implements PortalProvider {
    private final Set<Portal> portals = new HashSet<>();
    private final Path dataPath;

    public PaperPortalProvider(PortalsPlugin plugin) {
        this.dataPath = plugin.getDataPath().resolve("saves");
    }

    @Override
    public Path getDataFolder() {
        return dataPath;
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
        var portal = new PaperPortal(name, boundingBox);
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
        return portals.remove(portal);
    }

    @Override
    public boolean deletePortal(String name) {
        return portals.removeIf(portal -> portal.getName().equals(name));
    }
}
