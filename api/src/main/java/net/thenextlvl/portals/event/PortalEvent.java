package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Represents an event related to a portal.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public abstract class PortalEvent extends Event {
    private final Portal portal;

    @ApiStatus.Internal
    protected PortalEvent(Portal portal) {
        this.portal = portal;
    }

    /**
     * Gets the portal that triggered this event.
     *
     * @return the portal
     * @since 0.1.0
     */
    @Contract(pure = true)
    public Portal getPortal() {
        return portal;
    }
}
