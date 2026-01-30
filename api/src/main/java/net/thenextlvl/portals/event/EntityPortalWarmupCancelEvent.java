package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.time.Duration;

/**
 * This event is fired when an entity portal warmup is canceled.
 *
 * @since 1.2.0
 */
public final class EntityPortalWarmupCancelEvent extends PortalEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final Duration remaining;
    private final Entity entity;

    @ApiStatus.Internal
    public EntityPortalWarmupCancelEvent(Portal portal, Entity entity, Duration remaining) {
        super(portal);
        this.entity = entity;
        this.remaining = remaining;
    }

    /**
     * Gets the remaining duration of the portal warmup.
     *
     * @return the remaining duration
     * @since 1.2.0
     */
    @Contract(pure = true)
    public Duration getRemaining() {
        return remaining;
    }

    /**
     * Gets the entity that is warming up.
     *
     * @return the entity
     * @since 1.2.0
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
