package net.thenextlvl.portals.config;

public record SimplePortalConfig(
        boolean entryCosts,
        boolean ignoreEntityMovement,
        boolean pushBackOnEntryDenied,
        double pushBackSpeed
) implements PortalConfig {
    public static final SimplePortalConfig INSTANCE = new SimplePortalConfig(
            true, true, true, 0.3
    );
}
