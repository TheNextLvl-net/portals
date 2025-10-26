package net.thenextlvl.portals;

import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Get an instance of this class via {@link org.bukkit.plugin.ServicesManager#load(Class)}.
 * <p>
 * {@code PortalProvider provider = getServer().getServicesManager().load(PortalProvider.class); }
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface PortalProvider {
    @Contract(pure = true)
    Path getDataFolder();

    @Contract(pure = true)
    Optional<Portal> getPortal(String name);

    @Contract(pure = true)
    Stream<Portal> getPortals();

    @Contract(pure = true)
    Stream<Portal> getPortals(World world);

    // throws if a portal with the same name already exists
    @Contract(value = "_, _ -> new", mutates = "this")
    Portal createPortal(String name, BoundingBox boundingBox) throws IllegalArgumentException;

    @Contract(pure = true)
    boolean hasPortal(Portal portal);

    @Contract(pure = true)
    boolean hasPortal(String name);

    @Contract(mutates = "this")
    boolean deletePortal(Portal portal);

    @Contract(mutates = "this")
    boolean deletePortal(String name);
}
