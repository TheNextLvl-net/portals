package net.thenextlvl.portals.selection;

import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

/**
 * Provides selection functionality for players.
 * <p>
 * Get an instance of this class via {@link org.bukkit.plugin.ServicesManager#load(Class)}.
 * <p>
 * {@code SelectionProvider provider = getServer().getServicesManager().load(SelectionProvider.class); }
 *
 * @since 0.1.0
 */
public interface SelectionProvider {
    /**
     * Gets the current selection for a player.
     *
     * @param player the player for whom to get the selection
     * @return The current selection, or empty if none.
     * @since 0.1.0
     */
    @Contract(pure = true)
    Optional<BoundingBox> getSelection(Player player);

    /**
     * Sets the selection for a player.
     *
     * @param player      the player
     * @param boundingBox the bounding box to set as the selection
     * @since 0.1.0
     */
    void setSelection(Player player, BoundingBox boundingBox);

    /**
     * Removes the selection for a player.
     *
     * @param player the player
     * @return {@code true} if the selection was removed, {@code false} otherwise.
     * @since 0.1.0
     */
    boolean clearSelection(Player player);

    /**
     * Checks if a player has a selection.
     *
     * @param player the player
     * @return {@code true} if the player has a selection, {@code false} otherwise.
     * @since 0.1.0
     */
    @Contract(pure = true)
    boolean hasSelection(Player player);
}
