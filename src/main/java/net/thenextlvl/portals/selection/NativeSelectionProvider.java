package net.thenextlvl.portals.selection;

import net.thenextlvl.portals.shape.Shape;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class NativeSelectionProvider implements SelectionProvider {
    private final Map<UUID, Shape> selections = new ConcurrentHashMap<>();

    @Override
    public Optional<Shape> getSelection(Player player) {
        return Optional.ofNullable(selections.get(player.getUniqueId()));
    }

    @Override
    public void setSelection(Player player, Shape shape) {
        selections.put(player.getUniqueId(), shape);
    }

    @Override
    public boolean clearSelection(Player player) {
        return selections.remove(player.getUniqueId()) != null;
    }

    @Override
    public boolean hasSelection(Player player) {
        return selections.containsKey(player.getUniqueId());
    }
}
