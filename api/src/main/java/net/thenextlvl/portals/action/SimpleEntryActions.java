package net.thenextlvl.portals.action;

import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.action.teleport.Bounds;
import org.bukkit.Location;
import org.bukkit.World;

final class SimpleEntryActions implements EntryActions {
    public static final SimpleEntryActions INSTANCE = new SimpleEntryActions();

    @Override
    public EntryAction runCommand(String command) {
        return null; // todo: implement
    }

    @Override
    public EntryAction runConsoleCommand(String command) {
        return null; // todo: implement
    }

    @Override
    public EntryAction randomTeleport(World world, Bounds bounds) {
        return null; // todo: implement
    }

    @Override
    public EntryAction teleport(Location location) {
        return null; // todo: implement
    }

    @Override
    public EntryAction teleport(Portal portal) {
        return null; // todo: implement
    }

    @Override
    public EntryAction transfer(String hostname, int port) {
        return null; // todo: implement
    }

    @Override
    public EntryAction connect(String server) {
        return null; // todo: implement
    }
}

