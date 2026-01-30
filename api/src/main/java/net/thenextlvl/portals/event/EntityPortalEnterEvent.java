package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * This event is fired when an entity enters a portal and all checks succeeded.
 * Checks include permission, cooldown, and entry cost.
 * <p>
 * Canceling this event will prevent the entity from entering and using the portal.
 *
 * @see PreEntityPortalEnterEvent
 * @since 0.1.0
 */
public final class EntityPortalEnterEvent extends PortalEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();

    private final Entity entity;
    private boolean cancelled = false;

    @ApiStatus.Internal
    public EntityPortalEnterEvent(final Portal portal, final Entity entity) {
        super(portal);
        this.entity = entity;
    }

    /**
     * Gets the entity that entered the portal.
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
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    @Contract(mutates = "this")
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
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
