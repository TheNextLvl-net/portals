package net.thenextlvl.portals;

import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @since 0.1.0
 */
@NullMarked
public interface PortalProvider {
    @Contract(pure = true)
    Path getDataFolder();

    @Contract(pure = true)
    Optional<Portal> getPortal(String name);

    @Contract(pure = true)
    Stream<Portal> getPortals();

    @Contract(pure = true)
    Stream<Portal> getPortals(World world);

    @Contract(value = "_ -> new", mutates = "this")
    Portal createPortal(String name);

    @Contract(pure = true)
    boolean hasPortal(Portal portal);

    @Contract(pure = true)
    boolean hasPortal(String name);

    @Contract(mutates = "this")
    boolean removePortal(Portal portal);

    @Contract(mutates = "this")
    boolean removePortal(String name);
}
