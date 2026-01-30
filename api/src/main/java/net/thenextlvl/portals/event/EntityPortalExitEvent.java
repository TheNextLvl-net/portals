package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * This event is fired when an entity exits a portal.
 *
 * @since 0.1.0
 */
public final class EntityPortalExitEvent extends PortalEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final Entity entity;

    @ApiStatus.Internal
    public EntityPortalExitEvent(final Portal portal, final Entity entity) {
        super(portal);
        this.entity = entity;
    }

    /**
     * Gets the entity that exited the portal.
     *
     * @return the entity
     * @since 0.1.0
     */
    @Contract(pure = true)
    public Entity getEntity() {
        return entity;
    }

    @Override
    @Contract(pure = true)
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Contract(pure = true)
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
