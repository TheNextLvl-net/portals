package net.thenextlvl.portals;

import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.shape.BoundingBox;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface Portal {
    @Contract(pure = true)
    String getName();

    @Contract(pure = true)
    BoundingBox getBoundingBox();

    @Contract(mutates = "this")
    void setBoundingBox(BoundingBox boundingBox);

    @Contract(pure = true)
    Optional<String> getEntryPermission();

    @Contract(mutates = "this")
    void setEntryPermission(@Nullable String permission);

    @Contract(pure = true)
    Duration getCooldown();

    // throws if negative
    @Contract(mutates = "this")
    void setCooldown(Duration cooldown) throws IllegalArgumentException;

    @Contract(pure = true)
    double getEntryCost();

    // throws if negative
    @Contract(mutates = "this")
    void setEntryCost(double cost) throws IllegalArgumentException;

    @Contract(pure = true)
    Optional<EntryAction> getEntryAction();

    @Contract(mutates = "this")
    void setEntryAction(@Nullable EntryAction action);
}
