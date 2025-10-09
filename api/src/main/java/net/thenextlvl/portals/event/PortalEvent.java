package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class PortalEvent extends Event {
    private final Portal portal;

    protected PortalEvent(Portal portal) {
        this.portal = portal;
    }
    
    @Contract(pure = true)
    public Portal getPortal() {
        return portal;
    }
}
