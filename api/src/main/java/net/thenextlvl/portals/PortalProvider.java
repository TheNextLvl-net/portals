package net.thenextlvl.portals;

import net.thenextlvl.binder.StaticBinder;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Provides access to portal data.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface PortalProvider {
    /**
     * Gets the portal provider.
     *
     * @return the portal provider
     * @since 0.1.0
     */
    static @CheckReturnValue PortalProvider provider() {
        return StaticBinder.getInstance(PortalProvider.class.getClassLoader()).find(PortalProvider.class);
    }

    /**
     * Gets the portal data folder for the given world.
     *
     * @param world the world
     * @return the portal data folder
     * @since 0.1.0
     */
    @Contract(pure = true)
    Path getDataFolder(World world);

    /**
     * Gets the portal with the given name.
     *
     * @param name the name of the portal
     * @return an optional containing the portal, or empty if not found
     * @since 0.1.0
     */
    @Contract(pure = true)
    Optional<Portal> getPortal(String name);

    /**
     * Gets all portals.
     *
     * @return a stream of all portals
     * @since 0.1.0
     */
    @Contract(pure = true)
    Stream<Portal> getPortals();

    /**
     * Gets all portals in the given world.
     *
     * @param world the world
     * @return a stream of all portals in the world
     * @since 0.1.0
     */
    @Contract(pure = true)
    Stream<Portal> getPortals(World world);

    /**
     * Creates a new portal.
     *
     * @param name        the name of the portal
     * @param boundingBox the bounding box of the portal
     * @return the created portal
     * @throws IllegalArgumentException if a portal with the same name already exists
     * @since 0.1.0
     */
    @Contract(value = "_, _ -> new", mutates = "this")
    Portal createPortal(String name, BoundingBox boundingBox) throws IllegalArgumentException;

    /**
     * Checks if the given portal is still registered.
     *
     * @param portal the portal
     * @return {@code true} if the portal is still registered, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean hasPortal(Portal portal);

    /**
     * Checks if a portal with the given name exists.
     *
     * @param name the name of the portal
     * @return {@code true} if a portal with the given name exists, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean hasPortal(String name);

    /**
     * Deletes the given portal.
     *
     * @param portal the portal to delete
     * @return {@code true} if the portal was deleted, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this,io")
    boolean deletePortal(Portal portal);

    /**
     * Deletes the portal with the given name.
     *
     * @param name the name of the portal to delete
     * @return {@code true} if the portal was deleted, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this,io")
    boolean deletePortal(String name);

    /**
     * Performs the given action for each portal.
     *
     * @param action the action to perform
     * @since 0.1.0
     */
    void forEachPortal(Consumer<Portal> action);
}
