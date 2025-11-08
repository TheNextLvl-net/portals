package net.thenextlvl.portals.model;

import net.thenextlvl.binder.StaticBinder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;

/**
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface PortalConfig {
    static @CheckReturnValue PortalConfig config() {
        return StaticBinder.getInstance(PortalConfig.class.getClassLoader()).find(PortalConfig.class);
    }

    // whether to use economy for entry costs
    boolean entryCosts();

    // whether to ignore entity movement
    boolean ignoreEntityMovement();

    // whether to push back entities that are denied entry
    boolean pushBackOnEntryDenied();

    // speed at which entities are pushed back
    double pushBackSpeed();
}
