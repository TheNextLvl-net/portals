package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import org.bukkit.entity.Entity;

/**
 * @since 0.1.0
 */
public interface EntryAction {
    boolean onEntry(Portal portal, Entity entity);
}
