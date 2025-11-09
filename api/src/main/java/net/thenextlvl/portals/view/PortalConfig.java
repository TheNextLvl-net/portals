package net.thenextlvl.portals.view;

import net.thenextlvl.binder.StaticBinder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;

/**
 * Configuration for the portal plugin.
 *
 * @since 0.2.0
 */
@ApiStatus.NonExtendable
public interface PortalConfig {
    /**
     * Gets the portal configuration.
     *
     * @return the portal configuration
     * @since 0.2.0
     */
    static @CheckReturnValue PortalConfig config() {
        return StaticBinder.getInstance(PortalConfig.class.getClassLoader()).find(PortalConfig.class);
    }

    /**
     * Whether to allow random teleports to caves.
     *
     * @return {@code true} if cave and spawns are allowed, {@code false} otherwise
     * @since 0.2.0
     */
    boolean allowCaveSpawns();

    /**
     * Whether to use economy for entry costs.
     *
     * @return {@code true} if economy is used for entry costs, {@code false} otherwise
     * @since 0.2.0
     */
    boolean entryCosts();

    /**
     * Whether to ignore entity movement.
     *
     * @return {@code true} if entity movement is ignored, {@code false} otherwise
     * @since 0.2.0
     */
    boolean ignoreEntityMovement();

    /**
     * Speed at which entities are pushed back.
     * <p>
     * A value of 0 and below means this feature is disabled.
     *
     * @return the pushback speed
     * @since 0.2.0
     */
    double pushbackSpeed();
}
