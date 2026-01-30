package net.thenextlvl.portals.plugin.portal;

import com.google.common.base.Preconditions;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalProvider;
import net.thenextlvl.portals.plugin.PortalsPlugin;
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

import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class PaperPortalProvider implements PortalProvider {
    public final Set<Portal> portals = new HashSet<>();
    private final PortalsPlugin plugin;

    public PaperPortalProvider(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Path getDataFolder(final World world) {
        return world.getWorldPath().resolve("portals");
    }

    @Override
    public Optional<Portal> getPortal(final String name) {
        return getPortals().filter(portal -> portal.getName().equals(name)).findAny();
    }

    @Override
    public Stream<Portal> getPortals() {
        return portals.stream();
    }

    @Override
    public Stream<Portal> getPortals(final World world) {
        return getPortals().filter(portal -> portal.getWorld().equals(world));
    }

    @Override
    public Portal createPortal(final String name, final BoundingBox boundingBox) throws IllegalArgumentException {
        Preconditions.checkArgument(!hasPortal(name), "Portal with name '%s' already exists", name);
        final var portal = new PaperPortal(plugin, name, boundingBox);
        portals.add(portal);
        return portal;
    }

    @Override
    public boolean hasPortal(final Portal portal) {
        return portals.contains(portal);
    }

    @Override
    public boolean hasPortal(final String name) {
        return getPortals().anyMatch(portal -> portal.getName().equals(name));
    }

    @Override
    public boolean deletePortal(final Portal portal) {
        try {
            if (!portals.remove(portal)) return false;
            Files.deleteIfExists(portal.getDataFile());
            Files.deleteIfExists(portal.getBackupFile());
            return true;
        } catch (final IOException e) {
            plugin.getComponentLogger().error("Failed to delete portal {}", portal.getName(), e);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PortalsPlugin.ERROR_TRACKER.trackError(e);
            return false;
        }
    }

    @Override
    public boolean deletePortal(final String name) {
        return getPortal(name).map(this::deletePortal).orElse(false);
    }

    @Override
    public void forEachPortal(final Consumer<Portal> action) {
        portals.forEach(action);
    }
}
