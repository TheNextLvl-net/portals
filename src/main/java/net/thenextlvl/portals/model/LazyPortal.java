package net.thenextlvl.portals.model;

import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.PortalsPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;

@NullMarked
public final class LazyPortal implements PortalLike {
    private @Nullable WeakReference<Portal> portal = null;
    private final String name;

    public LazyPortal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<Portal> getPortal() {
        var plugin = JavaPlugin.getPlugin(PortalsPlugin.class);
        if (this.portal != null) {
            var portal = this.portal.get();
            if (portal != null) return Optional.of(portal);
        }
        var portal = plugin.portalProvider().getPortal(this.name);
        this.portal = portal.map(WeakReference::new).orElse(null);
        return portal;
    }
}
