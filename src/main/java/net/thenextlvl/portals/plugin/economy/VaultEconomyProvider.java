package net.thenextlvl.portals.plugin.economy;

import net.milkbowl.vault.economy.Economy;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Optional;

@NullMarked
public final class VaultEconomyProvider implements EconomyProvider {
    private final PortalsPlugin plugin;

    public VaultEconomyProvider(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    private Optional<Economy> getEconomy() {
        return Optional.ofNullable(plugin.getServer().getServicesManager().load(Economy.class));
    }

    @Override
    public String format(final Locale locale, final double amount) {
        return getEconomy().map(economy -> economy.format(amount))
                .orElseGet(() -> EconomyProvider.super.format(locale, amount));
    }

    @Override
    public boolean withdraw(final Player player, final double amount) {
        return getEconomy().map(economy -> {
            return economy.withdrawPlayer(player, amount).transactionSuccess();
        }).orElse(false);
    }
}
