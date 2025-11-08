package net.thenextlvl.portals.action;

import net.kyori.adventure.key.KeyPattern;

record SimpleActionType<T>(@KeyPattern.Value String name, Class<T> type, Action<T> action) implements ActionType<T> {
    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public @KeyPattern.Value String getName() {
        return name;
    }

    @Override
    public Action<T> getAction() {
        return action;
    }
}
