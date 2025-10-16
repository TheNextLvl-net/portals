package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * This event is fired when an entity enters a portal.
 * <p>
 * Canceling this event will prevent the entity from entering and using the portal.
 *
 * @since 0.1.0
 */
public final class EntityPortalEnterEvent extends PortalEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();

    private final Entity entity;
    private boolean cancelled = false;

    @ApiStatus.Internal
    public EntityPortalEnterEvent(Portal portal, Entity entity) {
        super(portal);
        this.entity = entity;
    }

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
    public void setCancelled(boolean cancelled) {
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
