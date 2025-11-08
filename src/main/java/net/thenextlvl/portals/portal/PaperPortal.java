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
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
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

    private Path dataFile;
    private Path backupFile;

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
    public boolean setBoundingBox(BoundingBox boundingBox) {
        if (this.boundingBox.equals(boundingBox)) return false;

        if (boundingBox.getWorld().equals(getWorld())) {
            this.boundingBox = boundingBox;
            return true;
        }

        var target = plugin.portalProvider().getDataFolder(boundingBox.getWorld());
        var dataFile = target.resolve(getDataFile().getFileName());
        var backupFile = target.resolve(getBackupFile().getFileName());

        try {
            Files.createDirectories(target);
            if (Files.exists(getDataFile())) Files.move(getDataFile(), dataFile, REPLACE_EXISTING);
            if (Files.exists(getBackupFile())) Files.move(getBackupFile(), backupFile, REPLACE_EXISTING);
        } catch (IOException e) {
            plugin.getComponentLogger().error("Failed to move portal data files for {}", getName(), e);
            return false;
        }

        this.dataFile = dataFile;
        this.backupFile = backupFile;
        this.boundingBox = boundingBox;
        return true;
    }

    @Override
    public Optional<String> getEntryPermission() {
        return Optional.ofNullable(entryPermission);
    }

    @Override
    public boolean setEntryPermission(@Nullable String permission) {
        if (Objects.equals(this.entryPermission, permission)) return false;
        this.entryPermission = permission;
        return true;
    }

    @Override
    public Duration getCooldown() {
        return cooldown;
    }

    @Override
    public boolean setCooldown(Duration cooldown) throws IllegalArgumentException {
        Preconditions.checkArgument(!cooldown.isNegative(), "Cooldown cannot be negative");
        if (this.cooldown.equals(cooldown)) return false;
        this.cooldown = cooldown;
        return true;
    }

    @Override
    public double getEntryCost() {
        return entryCost;
    }

    @Override
    public boolean setEntryCost(double cost) throws IllegalArgumentException {
        Preconditions.checkArgument(cost >= 0, "Entry cost cannot be negative");
        if (this.entryCost == cost) return false;
        this.entryCost = cost;
        return true;
    }

    @Override
    public Optional<EntryAction<?>> getEntryAction() {
        return Optional.ofNullable(entryAction);
    }

    @Override
    public boolean setEntryAction(@Nullable EntryAction<?> action) {
        if (Objects.equals(this.entryAction, action)) return false;
        this.entryAction = action;
        return true;
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
    public boolean setPersistent(boolean persistent) {
        if (this.persistent == persistent) return false;
        this.persistent = persistent;
        return true;
    }

    @Override
    public boolean persist() {
        if (!isPersistent()) return false;
        var file = getDataFile();
        var backup = getBackupFile();
        try {
            if (Files.isRegularFile(file)) Files.move(file, backup, REPLACE_EXISTING);
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
                Files.copy(backup, file, REPLACE_EXISTING);
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
