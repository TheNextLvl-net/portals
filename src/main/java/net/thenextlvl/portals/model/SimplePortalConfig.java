package net.thenextlvl.portals.model;

public record SimplePortalConfig(
        boolean entryCosts,
        boolean ignoreEntityMovement,
        boolean pushBackOnEntryDenied,
        double pushbackSpeed
) implements PortalConfig {
    public static final SimplePortalConfig INSTANCE = new SimplePortalConfig(
            true, false, true, 0.3
    );
}
