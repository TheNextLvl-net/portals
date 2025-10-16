package net.thenextlvl.portals.action;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;

/**
 * @since 0.1.0
 */
public sealed interface EntryActions permits SimpleEntryActions {
    @Contract(pure = true)
    static EntryActions actions() {
        return SimpleEntryActions.INSTANCE;
    }

    @Contract(pure = true)
    EntryAction runCommand(String command);

    @Contract(pure = true)
    EntryAction runConsoleCommand(String command);

    @Contract(pure = true)
    EntryAction teleport(Location location);

    @Contract(pure = true)
    EntryAction transfer(String hostname, int port);

    @Contract(pure = true)
    EntryAction connect(String server);
}
