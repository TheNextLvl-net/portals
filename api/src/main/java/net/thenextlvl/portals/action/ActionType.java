package net.thenextlvl.portals.action;

import net.kyori.adventure.key.KeyPattern;
import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
public sealed interface ActionType<T> permits SimpleActionType {
    @Contract(pure = true)
    Class<T> getType();

    @KeyPattern.Value
    @Contract(pure = true)
    String getName();

    @Contract(pure = true)
    Action<T> getAction();

    @FunctionalInterface
    interface Action<T> {
        boolean invoke(Entity entity, Portal portal, T input);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static <T> ActionType<T> create(@KeyPattern.Value String name, Class<T> type, Action<T> action) {
        return new SimpleActionType<>(name, type, action);
    }
}
