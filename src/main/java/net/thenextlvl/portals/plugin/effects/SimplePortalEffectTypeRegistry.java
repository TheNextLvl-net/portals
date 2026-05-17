package net.thenextlvl.portals.plugin.effects;

import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectType;
import net.thenextlvl.portals.effect.PortalEffectTypeRegistry;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NullMarked
public final class SimplePortalEffectTypeRegistry implements PortalEffectTypeRegistry {
    private final Set<PortalEffectType<?, ?>> types = new HashSet<>();

    @Override
    public boolean register(final PortalEffectType<?, ?> type) {
        return !isRegistered(type.getName()) && types.add(type);
    }

    @Override
    public boolean isRegistered(final String name) {
        return types.stream().anyMatch(type -> type.getName().equals(name));
    }

    @Override
    public boolean unregister(final PortalEffectType<?, ?> type) {
        return types.remove(type);
    }

    @Override
    public Optional<PortalEffectType<?, ?>> getByName(final String name) {
        return types.stream().filter(type -> type.getName().equals(name)).findAny();
    }

    @Override
    public Optional<PortalEffectType<?, ?>> getByEffect(final PortalEffect effect) {
        return types.stream().filter(type -> type.getType().isInstance(effect)).findAny();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PortalEffect, B extends PortalEffect.Builder<T, B>> Optional<PortalEffectType<T, B>> getByBuilder(final Class<B> builderType) {
        return types.stream()
                .filter(type -> type.getBuilderType().equals(builderType))
                .map(type -> (PortalEffectType<T, B>) type)
                .findAny();
    }

    @Override
    public <T extends PortalEffect, B extends PortalEffect.Builder<T, B>> Optional<B> builder(final Class<B> builderType) {
        return getByBuilder(builderType).map(PortalEffectType::builder);
    }

    @Override
    public @Unmodifiable Set<PortalEffectType<?, ?>> getTypes() {
        return Set.copyOf(types);
    }
}
