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
     * Whether to use the custom safe search algorithm.
     * <p>
     * If {@code false}, the vanilla safe search algorithm is used.
     * <p>
     * The custom safe search algorithm is less strict and allows cave and structure spawns
     * that might potentially be unsafe.
     *
     * @return {@code true} if the custom safe search algorithm is used, {@code false} otherwise
     * @since 0.2.0
     */
    boolean customSafeSearchAlgorithm();

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
     * Whether to push back entities that are denied entry.
     *
     * @return {@code true} if entities are pushed back, {@code false} otherwise
     * @since 0.2.0
     */
    boolean pushBackOnEntryDenied();

    /**
     * Speed at which entities are pushed back.
     *
     * @return the pushback speed
     * @since 0.2.0
     */
    double pushbackSpeed();
}
