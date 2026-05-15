package net.thenextlvl.portals.plugin.effects;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalEffectOrigin extends Location {
    private final long animationTick;
    private final double height;
    private final double width;

    public PortalEffectOrigin(final Location location, final double width, final double height) {
        this(location, width, height, 0);
    }

    public PortalEffectOrigin(final Location location, final double width, final double height, final long animationTick) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.width = width;
        this.height = height;
        this.animationTick = animationTick;
    }

    public long animationTick() {
        return animationTick;
    }

    public double height() {
        return height;
    }

    public double width() {
        return width;
    }
}
