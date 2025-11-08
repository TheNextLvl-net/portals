package net.thenextlvl.portals;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

/**
 * Represents a portal-like object.
 * <p>
 * Mainly used for deserialization and lazy loading of portal data.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface PortalLike {
    /**
     * Gets the name of the portal-like object.
     *
     * @return the name
     * @since 0.1.0
     */
    @Contract(pure = true)
    String getName();

    /**
     * Gets the portal.
     *
     * @return the portal
     * @since 0.1.0
     */
    @Contract(pure = true)
    Optional<Portal> getPortal();
}
