package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;

import java.util.Objects;

final class SimpleEntryAction<T> implements EntryAction<T> {
    private final ActionType<T> type;
    private T input;

    public SimpleEntryAction(final ActionType<T> type, final T input) {
        this.type = type;
        this.input = input;
    }

    @Override
    public ActionType<T> getActionType() {
        return type;
    }

    @Override
    public T getInput() {
        return input;
    }

    @Override
    public boolean setInput(final T input) {
        if (Objects.equals(this.input, input)) return false;
        this.input = input;
        return true;
    }

    @Override
    public boolean onEntry(final Entity entity, final Portal portal) {
        return type.getAction().invoke(entity, portal, input);
    }
}
