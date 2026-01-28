package net.thenextlvl.portals;

import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

/**
 * Represents a portal.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface Portal extends PortalLike {
    /**
     * Gets the world of the portal.
     *
     * @return the world
     * @since 0.1.0
     */
    @Contract(pure = true)
    World getWorld();

    /**
     * Gets the bounding box of the portal.
     *
     * @return the bounding box
     * @since 0.1.0
     */
    @Contract(pure = true)
    BoundingBox getBoundingBox();

    /**
     * Sets the bounding box of the portal.
     * <p>
     * Due to the file structure, this method may also move the data and backup file to the new world.
     * If an {@link java.io.IOException} occurs during the file operation,
     * the bounding box will not be changed and {@code false} is returned.
     *
     * @param boundingBox the bounding box
     * @return {@code true} if the bounding box was changed, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this,io")
    boolean setBoundingBox(BoundingBox boundingBox);

    /**
     * Gets the entry permission of the portal.
     *
     * @return the entry permission
     * @since 0.1.0
     */
    @Contract(pure = true)
    Optional<String> getEntryPermission();

    /**
     * Sets the entry permission of the portal.
     *
     * @param permission the entry permission
     * @return {@code true} if the entry permission was changed, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean setEntryPermission(@Nullable String permission);

    /**
     * Gets the cooldown of the portal.
     *
     * @return the cooldown
     * @since 0.1.0
     */
    @Contract(pure = true)
    Duration getCooldown();

    /**
     * Sets the cooldown of the portal.
     *
     * @param cooldown the cooldown
     * @return {@code true} if the cooldown was changed, {@code false} otherwise
     * @throws IllegalArgumentException if the cooldown is negative
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean setCooldown(Duration cooldown) throws IllegalArgumentException;

    /**
     * Gets the warmup time of the portal.
     * <p>
     * If positive, the portals entry action is executed after this delay.
     * The warmup can be cancelled by player actions (plugin-side logic).
     *
     * @return the warmup time
     * @since 1.2.0
     */
    @Contract(pure = true)
    Duration getWarmup();

    /**
     * Sets the warmup time of the portal.
     *
     * @param warmup the warmup time
     * @return {@code true} if the warmup time was changed, {@code false} otherwise
     * @throws IllegalArgumentException if the warmup is negative
     * @since 1.2.0
     */
    @Contract(mutates = "this")
    boolean setWarmup(Duration warmup) throws IllegalArgumentException;

    /**
     * Gets the entry cost of the portal.
     *
     * @return the entry cost
     * @since 0.1.0
     */
    @Contract(pure = true)
    double getEntryCost();

    /**
     * Sets the entry cost of the portal.
     *
     * @param cost the entry cost
     * @return {@code true} if the entry cost was changed, {@code false} otherwise
     * @throws IllegalArgumentException if the entry cost is negative
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean setEntryCost(double cost) throws IllegalArgumentException;

    /**
     * Gets the entry action of the portal.
     *
     * @return the entry action
     * @since 0.1.0
     */
    @Contract(pure = true)
    Optional<EntryAction<?>> getEntryAction();

    /**
     * Sets the entry action of the portal.
     *
     * @param action the entry action
     * @return {@code true} if the entry action was changed, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean setEntryAction(@Nullable EntryAction<?> action);

    /**
     * Gets the data file of the portal.
     *
     * @return the data file
     * @since 0.1.0
     */
    @Contract(pure = true)
    Path getDataFile();

    /**
     * Gets the backup file of the portal.
     *
     * @return the backup file
     * @since 0.1.0
     */
    @Contract(pure = true)
    Path getBackupFile();

    /**
     * Checks if the portal is persistent.
     *
     * @return {@code true} if the portal is persistent, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean isPersistent();

    /**
     * Sets the persistent state of the portal.
     *
     * @param persistent the persistent state
     * @return {@code true} if the persistence state was changed, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "this")
    boolean setPersistent(boolean persistent);

    /**
     * Persist the portal data.
     *
     * @return {@code true} if the portal data was persisted, {@code false} otherwise
     * @since 0.1.0
     */
    @Contract(mutates = "io")
    boolean persist();
}
