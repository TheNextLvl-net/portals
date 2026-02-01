package net.thenextlvl.portals.plugin.portal;

import com.google.common.base.Preconditions;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.audience.Audience;
import net.thenextlvl.nbt.NBTOutputStream;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.notification.Notification;
import net.thenextlvl.portals.notification.NotificationTrigger;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.notification.Notifications;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.model.SimpleNotification;
import net.thenextlvl.portals.plugin.utils.Debugger;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class PaperPortal implements Portal {
    public static final Map<UUID, Warmup> WARMUPS = new HashMap<>();

    private final SimpleNotifications notifications = new SimpleNotifications();
    private final Map<UUID, Instant> cooldowns = new HashMap<>();

    private final PortalsPlugin plugin;
    private final String name;

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
    public Duration getRemainingCooldown(final Entity entity) {
        final var finished = this.cooldowns.get(entity.getUniqueId());
        if (finished == null) return Duration.ZERO;
        return Duration.between(Instant.now(), finished);
    }

    public void startCooldown(final Entity entity) {
        cooldowns.put(entity.getUniqueId(), Instant.now().plus(cooldown));
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
    public Duration getRemainingWarmup(final Entity entity) {
        final var warmup = WARMUPS.get(entity.getUniqueId());
        if (warmup == null) return Duration.ZERO;
        if (!warmup.portal.equals(this)) return Duration.ZERO;
        return Duration.between(Instant.now(), warmup.finished());
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
    public String getFormattedEntryCost(final Audience audience) {
        return plugin.economyProvider().format(audience, entryCost);
    }

    @Override
    public String getFormattedEntryCost(final Locale locale) {
        return plugin.economyProvider().format(locale, entryCost);
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
    public SimpleNotifications getNotifications() {
        return notifications;
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

    public record Warmup(Portal portal, Instant finished, ScheduledTask task, Debugger.Transaction transaction) {
    }

    public class SimpleNotifications implements Notifications {
        private final List<Notification<?>> notifications = new CopyOnWriteArrayList<>();

        @Override
        public Stream<Notification<?>> stream() {
            return notifications.stream();
        }

        @Override
        public Stream<Notification<?>> findByTrigger(final NotificationTrigger trigger) {
            return stream().filter(notification -> notification.trigger().equals(trigger));
        }

        @Override
        public boolean isEmpty() {
            return notifications.isEmpty();
        }

        @Override
        public int size() {
            return notifications.size();
        }

        @Override
        public void trigger(final NotificationTrigger trigger, final Entity entity) {
            findByTrigger(trigger).forEach(notification -> notification.send(entity, PaperPortal.this));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Optional<T> get(final NotificationTrigger trigger, final NotificationType<T> type) {
            return (Optional<T>) findByTrigger(trigger)
                    .filter(notification -> notification.type().equals(type))
                    .map(Notification::input)
                    .findAny();
        }

        @Override
        public <T> boolean set(final NotificationTrigger trigger, final NotificationType<T> type, final T input) {
            if (get(trigger, type).filter(t -> t.equals(input)).isPresent()) return false;
            remove(trigger, type);
            notifications.add(new SimpleNotification<>(trigger, type, input));
            return true;
        }

        @Override
        public boolean remove(final NotificationTrigger trigger, final NotificationType<?> type) {
            return notifications.removeIf(n -> n.trigger().equals(trigger) && n.type().equals(type));
        }

        @Override
        public Iterator<Notification<?>> iterator() {
            return notifications.iterator();
        }

        public Tag serialize(final TagSerializationContext context) throws ParserException {
            return ListTag.of(CompoundTag.ID, stream().map(notification -> {
                return CompoundTag.builder()
                        .put("trigger", context.serialize(notification.trigger()))
                        .put("type", context.serialize(notification.type()))
                        .put("input", context.serialize(notification.input()))
                        .build();
            }).toList());
        }

        @SuppressWarnings("unchecked")
        public void deserialize(final ListTag<CompoundTag> list, final TagDeserializationContext context) throws ParserException {
            list.forEach(compound -> {
                final var trigger = context.deserialize(compound.get("trigger"), NotificationTrigger.class);
                final var type = (NotificationType<Object>) context.deserialize(compound.get("type"), NotificationType.class);
                final var input = context.deserialize(compound.get("input"), type.getType());
                set(trigger, type, input);
            });
        }
    }
}
