package net.thenextlvl.portals.selection;

import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

/**
 * Get an instance of this class via {@link org.bukkit.plugin.ServicesManager#load(Class)}.
 * <p>
 * {@code SelectionProvider provider = getServer().getServicesManager().load(SelectionProvider.class); }
 *
 * @since 0.1.0
 */
public interface SelectionProvider {
    @Contract(pure = true)
    Optional<BoundingBox> getSelection(Player player);

    void setSelection(Player player, BoundingBox boundingBox);

    boolean clearSelection(Player player);

    @Contract(pure = true)
    boolean hasSelection(Player player);
}
