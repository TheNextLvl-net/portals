package net.thenextlvl.portals.plugin.model;

import net.thenextlvl.portals.view.PortalConfig;

public record SimplePortalConfig(
        boolean allowCaveSpawns,
        boolean entryCosts,
        boolean ignoreEntityMovement,
        double pushbackSpeed
) implements PortalConfig {
}
