package net.thenextlvl.portals.event;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.time.Duration;

/**
 * This event is fired when an entity portal warmup starts.
 *
 * @since 1.2.0
 */
public final class EntityPortalWarmupEvent extends PortalEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final Duration warmup;
    private final Entity entity;

    @ApiStatus.Internal
    public EntityPortalWarmupEvent(Portal portal, Entity entity) {
        super(portal);
        this.entity = entity;
        this.warmup = portal.getWarmup();
    }

    /**
     * Gets the warmup duration of the portal.
     *
     * @return the warmup duration
     * @since 1.2.0
     */
    @Contract(pure = true)
    public Duration getWarmup() {
        return warmup;
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
