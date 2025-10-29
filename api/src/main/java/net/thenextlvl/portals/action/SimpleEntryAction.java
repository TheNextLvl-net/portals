package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleEntryAction<T> implements EntryAction<T> {
    private final ActionType<T> type;
    private T input;

    public SimpleEntryAction(ActionType<T> type, T input) {
        this.type = type;
        this.input = input;
    }

    @Override
    public ActionType<T> getType() {
        return type;
    }

    @Override
    public T getInput() {
        return input;
    }

    @Override
    public boolean setInput(T input) {
        this.input = input;
        return true;
    }

    @Override
    public boolean onEntry(Entity entity, Portal portal) {
        return type.action().invoke(entity, portal, input);
    }
}
