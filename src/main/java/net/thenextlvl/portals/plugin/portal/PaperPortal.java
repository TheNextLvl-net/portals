package net.thenextlvl.portals.plugin.portal;

import com.google.common.base.Preconditions;
import net.thenextlvl.nbt.NBTOutputStream;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class PaperPortal implements Portal {
    private final PortalsPlugin plugin;
    private final String name;

    private final Map<NotificationTrigger, Map<NotificationType<Object>, Object>> notifications = new ConcurrentHashMap<>();

    private BoundingBox boundingBox;
    private Duration cooldown = Duration.ZERO;
    private Duration warmup = Duration.ZERO;

    private @Nullable String entryPermission = null;
    private @Nullable EntryAction<?> entryAction = null;

    private double entryCost = 0.0;
    private boolean persistent = true;

    private Path dataFile;
    private Path backupFile;

    public PaperPortal(final PortalsPlugin plugin, final String name, final BoundingBox boundingBox) {
        this.plugin = plugin;
        this.name = name;
        this.boundingBox = boundingBox;

        final var dataFolder = plugin.portalProvider().getDataFolder(getWorld());
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
    public boolean setBoundingBox(final BoundingBox boundingBox) {
        if (this.boundingBox.equals(boundingBox)) return false;

        if (boundingBox.getWorld().equals(getWorld())) {
            this.boundingBox = boundingBox;
            return true;
        }

        final var target = plugin.portalProvider().getDataFolder(boundingBox.getWorld());
        final var dataFile = target.resolve(getDataFile().getFileName());
        final var backupFile = target.resolve(getBackupFile().getFileName());

        try {
            Files.createDirectories(target);
            if (Files.exists(getDataFile())) Files.move(getDataFile(), dataFile, REPLACE_EXISTING);
            if (Files.exists(getBackupFile())) Files.move(getBackupFile(), backupFile, REPLACE_EXISTING);
        } catch (final IOException e) {
            plugin.getComponentLogger().error("Failed to move portal data files for {}", getName(), e);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PortalsPlugin.ERROR_TRACKER.trackError(e);
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
    public boolean setEntryPermission(@Nullable final String permission) {
        if (Objects.equals(this.entryPermission, permission)) return false;
        this.entryPermission = permission;
        return true;
    }

    @Override
    public Duration getCooldown() {
        return cooldown;
    }

    @Override
    public boolean setCooldown(final Duration cooldown) throws IllegalArgumentException {
        Preconditions.checkArgument(!cooldown.isNegative(), "Cooldown cannot be negative");
        if (this.cooldown.equals(cooldown)) return false;
        this.cooldown = cooldown;
        return true;
    }

    @Override
    public Duration getWarmup() {
        return warmup;
    }

    @Override
    public boolean setWarmup(final Duration warmup) throws IllegalArgumentException {
        Preconditions.checkArgument(!warmup.isNegative(), "Warmup cannot be negative");
        if (this.warmup.equals(warmup)) return false;
        this.warmup = warmup;
        return true;
    }

    @Override
    public double getEntryCost() {
        return entryCost;
    }

    @Override
    public boolean setEntryCost(final double cost) throws IllegalArgumentException {
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
    public boolean setEntryAction(@Nullable final EntryAction<?> action) {
        if (Objects.equals(this.entryAction, action)) return false;
        this.entryAction = action;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getNotification(final NotificationTrigger trigger, final NotificationType<T> type) {
        final var notifications = this.notifications.get(trigger);
        return notifications != null ? Optional.ofNullable((T) notifications.get(type)) : Optional.empty();
    }

    @Override
    public @Unmodifiable Set<NotificationTrigger> getNotificationTriggers() {
        return Set.copyOf(notifications.keySet());
    }

    @Override
    public @Unmodifiable Set<NotificationType<?>> getNotificationTypes(final NotificationTrigger trigger) {
        final var notifications = this.notifications.get(trigger);
        if (notifications == null) return Set.of();
        return Set.copyOf(notifications.keySet());
    }

    @Override
    public @Unmodifiable Map<NotificationType<?>, ?> getNotifications(final NotificationTrigger trigger) {
        final var notifications = this.notifications.get(trigger);
        return notifications != null ? Map.copyOf(notifications) : Map.of();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean setNotification(final NotificationTrigger trigger, final NotificationType<T> type, final T input) {
        final var put = notifications.computeIfAbsent(trigger, k -> new ConcurrentHashMap<>())
                .put((NotificationType<Object>) type, input);
        return put == null || !put.equals(input);
    }

    @Override
    public boolean removeNotification(final NotificationTrigger trigger, final NotificationType<?> notification) {
        final var notifications = this.notifications.get(trigger);
        if (notifications == null) return false;

        final var removed = notifications.remove(notification) != null;
        if (notifications.isEmpty()) this.notifications.remove(trigger);

        return removed;
    }

    @Override
    public boolean clearNotifications(final NotificationTrigger trigger) {
        return notifications.remove(trigger) != null;
    }

    @Override
    public void triggerNotification(final NotificationTrigger trigger, final Entity entity) {
        final var notifications = this.notifications.get(trigger);
        if (notifications == null) return;
        notifications.forEach((type, input) -> type.getSender().send(entity, this, input));
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
    public boolean setPersistent(final boolean persistent) {
        if (this.persistent == persistent) return false;
        this.persistent = persistent;
        return true;
    }

    @Override
    public boolean persist() {
        if (!isPersistent()) return false;
        final var file = getDataFile();
        final var backup = getBackupFile();
        try {
            if (Files.isRegularFile(file)) Files.move(file, backup, REPLACE_EXISTING);
            else Files.createDirectories(file.getParent());
            try (final var outputStream = NBTOutputStream.create(file)) {
                outputStream.writeTag(getName(), plugin.nbt(getWorld()).serialize(this));
                return true;
            }
        } catch (final Throwable t) {
            if (Files.isRegularFile(backup)) try {
                Files.copy(backup, file, REPLACE_EXISTING);
                plugin.getComponentLogger().warn("Recovered portal {} from potential data loss", getName());
            } catch (final IOException e) {
                plugin.getComponentLogger().error("Failed to restore portal {}", getName(), e);
            }
            plugin.getComponentLogger().error("Failed to save portal {}", getName(), t);
            plugin.getComponentLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PortalsPlugin.ERROR_TRACKER.trackError(t);
            return false;
        }
    }

    @Override
    public String toString() {
        return "PaperPortal{" +
                "name='" + name + '\'' +
                ", boundingBox=" + boundingBox +
                ", cooldown=" + cooldown +
                ", warmup=" + warmup +
                ", entryPermission='" + entryPermission + '\'' +
                ", entryCost=" + entryCost +
                ", persistent=" + persistent +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final PaperPortal that = (PaperPortal) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
