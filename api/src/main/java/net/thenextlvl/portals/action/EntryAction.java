package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
public sealed interface EntryAction<T> permits SimpleEntryAction {
    @Contract(pure = true)
    ActionType<T> getActionType();

    @Contract(pure = true)
    T getInput();

    @Contract(mutates = "this")
    boolean setInput(T input);

    boolean onEntry(Entity entity, Portal portal);

    @Contract(value = "_, _ -> new", pure = true)
    static <T> EntryAction<T> create(ActionType<T> actionType, T input) {
        return new SimpleEntryAction<>(actionType, input);
    }
}
