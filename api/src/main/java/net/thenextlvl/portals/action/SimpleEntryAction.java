package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleEntryAction<T> implements EntryAction<T> {
    private final ActionType<T> actionType;
    private T input;

    public SimpleEntryAction(ActionType<T> actionType, T input) {
        this.actionType = actionType;
        this.input = input;
    }

    @Override
    public ActionType<T> getActionType() {
        return actionType;
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
        return actionType.action().invoke(entity, portal, input);
    }
}
