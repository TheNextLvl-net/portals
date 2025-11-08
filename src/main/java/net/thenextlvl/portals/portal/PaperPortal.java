package net.thenextlvl.portals.portal;

import com.google.common.base.Preconditions;
import net.thenextlvl.nbt.NBTOutputStream;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

@NullMarked
public final class PaperPortal implements Portal {
    private final PortalsPlugin plugin;
    private final String name;

    private BoundingBox boundingBox;
    private Duration cooldown = Duration.ZERO;

    private @Nullable String entryPermission = null;
    private @Nullable EntryAction<?> entryAction = null;

    private double entryCost = 0.0;
    private boolean persistent = true;

    private final Path dataFile;
    private final Path backupFile;

    public PaperPortal(PortalsPlugin plugin, String name, BoundingBox boundingBox) {
        this.plugin = plugin;
        this.name = name;
        this.boundingBox = boundingBox;

        var dataFolder = plugin.portalProvider().getDataFolder(getWorld());
        this.dataFile = dataFolder.resolve(name + ".dat");
        this.backupFile = dataFolder.resolve(name + ".dat_old");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<Portal> getPortal() {
        return Optional.of(this);
    }

    @Override
    public World getWorld() {
        return boundingBox.getWorld();
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
    public Optional<EntryAction<?>> getEntryAction() {
        return Optional.ofNullable(entryAction);
    }

    @Override
    public void setEntryAction(@Nullable EntryAction<?> action) {
        this.entryAction = action;
    }

    @Override
    public Path getDataFile() {
        return dataFile;
    }

    @Override
    public Path getBackupFile() {
        return backupFile;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    @Override
    public boolean persist() {
        if (!isPersistent()) return false;
        var file = getDataFile();
        var backup = getBackupFile();
        try {
            if (Files.isRegularFile(file)) Files.move(file, backup, StandardCopyOption.REPLACE_EXISTING);
            else Files.createDirectories(file.getParent());
            try (var outputStream = new NBTOutputStream(
                    Files.newOutputStream(file, WRITE, CREATE, TRUNCATE_EXISTING),
                    StandardCharsets.UTF_8
            )) {
                outputStream.writeTag(getName(), plugin.nbt(getWorld()).serialize(this));
                return true;
            }
        } catch (Throwable t) {
            if (Files.isRegularFile(backup)) try {
                Files.copy(backup, file, StandardCopyOption.REPLACE_EXISTING);
                plugin.getComponentLogger().warn("Recovered portal {} from potential data loss", getName());
            } catch (IOException e) {
                plugin.getComponentLogger().error("Failed to restore portal {}", getName(), e);
            }
            plugin.getComponentLogger().error("Failed to save portal {}", getName(), t);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", PortalsPlugin.ISSUES);
            return false;
        }
    }

    @Override
    public String toString() {
        return "PaperPortal{" +
                "name='" + name + '\'' +
                ", boundingBox=" + boundingBox +
                ", cooldown=" + cooldown +
                ", entryPermission='" + entryPermission + '\'' +
                ", entryCost=" + entryCost +
                ", persistent=" + persistent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaperPortal that = (PaperPortal) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
