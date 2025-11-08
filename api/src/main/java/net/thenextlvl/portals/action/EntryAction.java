package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

/**
 * @since 0.1.0
 */
public sealed interface EntryAction<T> permits SimpleEntryAction {
    @Contract(pure = true)
    ActionType<T> getActionType();

    @Contract(pure = true)
    T getInput();

    @Contract(mutates = "this")
    boolean setInput(T input);

    boolean onEntry(Entity entity, Portal portal);

    @Contract(value = "_, _ -> new", pure = true)
    static <T> EntryAction<T> create(ActionType<T> type, T input) {
        return new SimpleEntryAction<>(type, input);
    }
}
