package net.thenextlvl.portals.model;

public record SimplePortalConfig(
        boolean entryCosts,
        boolean ignoreEntityMovement,
        boolean pushBackOnEntryDenied,
        double pushbackSpeed
) implements PortalConfig {
}
