package net.thenextlvl.portals.effect;

import org.bukkit.Color;
import org.bukkit.Particle;

import java.time.Duration;

public abstract class SimplePortalEffect implements PortalEffect {
    private final Duration duration;
    private final Particle particle;
    private final Color color;
    private final int particleCount;
    private final Duration updateInterval;
    private final double speed;

    protected SimplePortalEffect(Duration duration, Particle particle, Color color, int particleCount, Duration updateInterval, double speed) {
        this.duration = duration;
        this.particle = particle;
        this.color = color;
        this.particleCount = particleCount;
        this.updateInterval = updateInterval;
        this.speed = speed;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public Particle getParticle() {
        return particle;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getParticleCount() {
        return particleCount;
    }

    @Override
    public Duration getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public double getSpeed() {
        return speed;
    }
}
