package net.thenextlvl.portals.action;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NullMarked
final class SimpleActionTypeRegistry implements ActionTypeRegistry {
    public static final ActionTypeRegistry INSTANCE = new SimpleActionTypeRegistry();

    private final Set<ActionType<?>> actionTypes = new HashSet<>(Set.of(
            ActionTypes.types().connect(),
            ActionTypes.types().runCommand(),
            ActionTypes.types().runConsoleCommand(),
            ActionTypes.types().teleport(),
            ActionTypes.types().teleportPortal(),
            ActionTypes.types().teleportRandom(),
            ActionTypes.types().transfer()
    ));

    @Override
    public boolean register(ActionType<?> type) {
        return !isRegistered(type.getName()) && actionTypes.add(type);
    }

    @Override
    public boolean isRegistered(ActionType<?> type) {
        return actionTypes.contains(type);
    }

    @Override
    public boolean isRegistered(String name) {
        return actionTypes.stream().anyMatch(actionType -> actionType.getName().equals(name));
    }

    @Override
    public boolean unregister(ActionType<?> type) {
        return actionTypes.remove(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<ActionType<T>> getByName(String name) {
        return actionTypes.stream()
                .filter(actionType -> actionType.getName().equals(name))
                .map(actionType -> (ActionType<T>) actionType)
                .findAny();
    }

    @Override
    public @Unmodifiable Set<ActionType<?>> getActionTypes() {
        return Set.copyOf(actionTypes);
    }
}
