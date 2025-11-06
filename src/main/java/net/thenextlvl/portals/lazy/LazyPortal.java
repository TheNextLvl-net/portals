package net.thenextlvl.portals.lazy;

import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

@NullMarked
public class LazyPortal implements Portal {
    private @Nullable WeakReference<Portal> portal;
    private final String name;

    public LazyPortal(Portal portal) {
        this.portal = new WeakReference<>(portal);
        this.name = portal.getName();
    }

    public LazyPortal(String name) {
        this.portal = null;
        this.name = name;
    }

    private Optional<Portal> getPortal() {
        var plugin = JavaPlugin.getPlugin(PortalsPlugin.class);
        if (this.portal != null) {
            var portal = this.portal.get();
            if (portal != null) return Optional.of(portal);
        }
        var portal = plugin.portalProvider().getPortal(this.name);
        this.portal = portal.map(WeakReference::new).orElse(null);
        return portal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public World getWorld() {
        return getPortal().map(Portal::getWorld).orElseThrow();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getPortal().map(Portal::getBoundingBox).orElseThrow();
    }

    @Override
    public void setBoundingBox(BoundingBox boundingBox) {
        getPortal().ifPresent(portal -> portal.setBoundingBox(boundingBox));
    }

    @Override
    public Optional<String> getEntryPermission() {
        return getPortal().flatMap(Portal::getEntryPermission);
    }

    @Override
    public void setEntryPermission(@Nullable String permission) {
        getPortal().ifPresent(portal -> portal.setEntryPermission(permission));
    }

    @Override
    public Duration getCooldown() {
        return getPortal().map(Portal::getCooldown).orElseThrow();
    }

    @Override
    public void setCooldown(Duration cooldown) throws IllegalArgumentException {
        getPortal().ifPresent(portal -> portal.setCooldown(cooldown));
    }

    @Override
    public double getEntryCost() {
        return getPortal().map(Portal::getEntryCost).orElseThrow();
    }

    @Override
    public void setEntryCost(double cost) throws IllegalArgumentException {
        getPortal().ifPresent(portal -> portal.setEntryCost(cost));
    }

    @Override
    public Optional<EntryAction<?>> getEntryAction() {
        return getPortal().flatMap(Portal::getEntryAction);
    }

    @Override
    public void setEntryAction(@Nullable EntryAction<?> action) {
        getPortal().ifPresent(portal -> portal.setEntryAction(action));
    }

    @Override
    public Path getDataFile() {
        return getPortal().map(Portal::getDataFile).orElseThrow();
    }

    @Override
    public Path getBackupFile() {
        return getPortal().map(Portal::getBackupFile).orElseThrow();
    }

    @Override
    public boolean isPersistent() {
        return getPortal().map(Portal::isPersistent).orElseThrow();
    }

    @Override
    public void setPersistent(boolean persistent) {
        getPortal().ifPresent(portal -> portal.setPersistent(persistent));
    }

    @Override
    public boolean persist() {
        return getPortal().map(Portal::persist).orElseThrow();
    }
}
