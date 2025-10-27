package net.thenextlvl.portals.config;

import org.jetbrains.annotations.ApiStatus;

/**
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface PortalConfig {
    // whether to use economy for entry costs
    boolean entryCosts();

    // whether to ignore entity movement
    boolean ignoreEntityMovement();

    // whether to push back entities that are denied entry
    boolean pushBackOnEntryDenied();

    // speed at which entities are pushed back
    double pushBackSpeed();
}
