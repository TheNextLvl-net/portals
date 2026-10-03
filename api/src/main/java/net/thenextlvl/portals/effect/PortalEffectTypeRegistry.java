package net.thenextlvl.portals.effect;

import net.thenextlvl.binder.StaticBinder;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;

public interface PortalEffectTypeRegistry {
    @Contract(pure = true)
    static @CheckReturnValue PortalEffectTypeRegistry registry() {
        return StaticBinder.getInstance(PortalEffectTypeRegistry.class.getClassLoader()).find(PortalEffectTypeRegistry.class);
    }

    @Contract(mutates = "this")
    boolean register(PortalEffectType<?, ?> type);

    @Contract(pure = true)
    boolean isRegistered(String name);

    @Contract(mutates = "this")
    boolean unregister(PortalEffectType<?, ?> type);

    @Contract(pure = true)
    Optional<PortalEffectType<?, ?>> getByName(String name);

    @Contract(pure = true)
    Optional<PortalEffectType<?, ?>> getByEffect(PortalEffect effect);

    @Contract(pure = true)
    <T extends PortalEffect, B extends PortalEffect.Builder<T, B>> Optional<PortalEffectType<T, B>> getByBuilder(Class<B> builderType);

    @Contract(value = "_ -> new", pure = true)
    <T extends PortalEffect, B extends PortalEffect.Builder<T, B>> Optional<B> builder(Class<B> builderType);

    @Unmodifiable
    @Contract(pure = true)
    Set<PortalEffectType<?, ?>> getTypes();
}
