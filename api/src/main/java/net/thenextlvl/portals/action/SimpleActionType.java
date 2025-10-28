package net.thenextlvl.portals.action;

import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
record SimpleActionType<T>(@KeyPattern.Value String name, Class<T> type, Action<T> action) implements ActionType<T> {
}
