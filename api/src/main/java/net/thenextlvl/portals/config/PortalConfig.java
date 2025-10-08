package net.thenextlvl.portals.config;

import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
public interface PortalConfig {
    // whether to use economy for entry costs
    boolean entryCosts();

    // whether to push back entities that are denied entry
    boolean pushBackOnEntryDenied();
}
