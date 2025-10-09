package net.thenextlvl.portals.config;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface PortalConfig {
    // whether to use economy for entry costs
    boolean entryCosts();

    // whether to push back entities that are denied entry
    boolean pushBackOnEntryDenied();
}
