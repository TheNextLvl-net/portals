package net.thenextlvl.portals.portal;

import com.google.common.base.Preconditions;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.shape.BoundingBox;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@NullMarked
public final class PaperPortal implements Portal {
    private final String name;

    private BoundingBox boundingBox;
    private Duration cooldown = Duration.ZERO;

    private @Nullable String entryPermission = null;
    private @Nullable EntryAction entryAction = null;

    private double entryCost = 0.0;

    public PaperPortal(String name, BoundingBox boundingBox) {
        this.name = name;
        this.boundingBox = boundingBox;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public Optional<String> getEntryPermission() {
        return Optional.ofNullable(entryPermission);
    }

    @Override
    public void setEntryPermission(@Nullable String permission) {
        this.entryPermission = permission;
    }

    @Override
    public Duration getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(Duration cooldown) throws IllegalArgumentException {
        Preconditions.checkArgument(!cooldown.isNegative(), "Cooldown cannot be negative");
        this.cooldown = cooldown;
    }

    @Override
    public double getEntryCost() {
        return entryCost;
    }

    @Override
    public void setEntryCost(double cost) throws IllegalArgumentException {
        Preconditions.checkArgument(cost >= 0, "Entry cost cannot be negative");
        this.entryCost = cost;
    }

    @Override
    public Optional<EntryAction> getEntryAction() {
        return Optional.ofNullable(entryAction);
    }

    @Override
    public void setEntryAction(@Nullable EntryAction action) {
        this.entryAction = action;
    }

    @Override
    public String toString() {
        return "PaperPortal{" +
                "name='" + name + '\'' +
                ", boundingBox=" + boundingBox +
                ", cooldown=" + cooldown +
                ", entryPermission='" + entryPermission + '\'' +
                ", entryCost=" + entryCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaperPortal that = (PaperPortal) o;
        return Double.compare(entryCost, that.entryCost) == 0 && Objects.equals(name, that.name) && Objects.equals(boundingBox, that.boundingBox) && Objects.equals(cooldown, that.cooldown) && Objects.equals(entryPermission, that.entryPermission) && Objects.equals(entryAction, that.entryAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, boundingBox, cooldown, entryPermission, entryAction, entryCost);
    }
}
