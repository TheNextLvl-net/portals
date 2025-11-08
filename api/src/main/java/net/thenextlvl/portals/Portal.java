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
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface Portal extends PortalLike {
    @Contract(pure = true)
    World getWorld();

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
     * @return {@code true} if the bounding box was changed
     * @since 0.1.0
     */
    @Contract(mutates = "this,io")
    boolean setBoundingBox(BoundingBox boundingBox);

    @Contract(pure = true)
    Optional<String> getEntryPermission();

    @Contract(mutates = "this")
    boolean setEntryPermission(@Nullable String permission);

    @Contract(pure = true)
    Duration getCooldown();

    // throws if negative
    @Contract(mutates = "this")
    boolean setCooldown(Duration cooldown) throws IllegalArgumentException;

    @Contract(pure = true)
    double getEntryCost();

    // throws if negative
    @Contract(mutates = "this")
    boolean setEntryCost(double cost) throws IllegalArgumentException;

    @Contract(pure = true)
    Optional<EntryAction<?>> getEntryAction();

    @Contract(mutates = "this")
    boolean setEntryAction(@Nullable EntryAction<?> action);

    @Contract(pure = true)
    Path getDataFile();

    @Contract(pure = true)
    Path getBackupFile();

    @Contract(pure = true)
    boolean isPersistent();

    @Contract(mutates = "this")
    boolean setPersistent(boolean persistent);

    @Contract(mutates = "io")
    boolean persist();
}
