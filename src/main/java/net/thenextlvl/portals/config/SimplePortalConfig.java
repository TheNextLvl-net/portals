package net.thenextlvl.portals.config;

public record SimplePortalConfig(
        boolean entryCosts,
        boolean pushBackOnEntryDenied,
        double pushBackSpeed
) implements PortalConfig {
    public static final SimplePortalConfig INSTANCE = new SimplePortalConfig(
            true, true, 0.3
    );
}
