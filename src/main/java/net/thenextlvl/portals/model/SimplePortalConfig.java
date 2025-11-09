package net.thenextlvl.portals.model;

import net.thenextlvl.portals.view.PortalConfig;

public record SimplePortalConfig(
        boolean allowCaveSpawns,
        boolean entryCosts,
        boolean ignoreEntityMovement,
        boolean pushBackOnEntryDenied,
        double pushbackSpeed
) implements PortalConfig {
}
