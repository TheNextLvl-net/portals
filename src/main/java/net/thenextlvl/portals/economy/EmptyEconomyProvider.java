package net.thenextlvl.portals.economy;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EmptyEconomyProvider implements EconomyProvider {
    @Override
    public boolean withdraw(Player player, double amount) {
        return false;
    }
}
