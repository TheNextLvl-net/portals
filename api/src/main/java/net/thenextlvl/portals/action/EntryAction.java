package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
public interface EntryAction {
    boolean onEntry(Portal portal, Entity entity);
}
