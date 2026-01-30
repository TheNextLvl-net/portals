package net.thenextlvl.portals.plugin.economy;

import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.service.api.economy.EconomyController;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Optional;

@NullMarked
public final class ServiceEconomyProvider implements EconomyProvider {
    private final PortalsPlugin plugin;

    public ServiceEconomyProvider(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    private Optional<EconomyController> getController() {
        return Optional.ofNullable(plugin.getServer().getServicesManager().load(EconomyController.class));
    }

    @Override
    public String format(final Locale locale, final double amount) {
        return getController().map(controller -> controller.format(amount))
                .orElseGet(() -> EconomyProvider.super.format(locale, amount));
    }

    @Override
    public boolean withdraw(final Player player, final double amount) {
        return getController().flatMap(controller -> controller.getAccount(player).map(account -> {
            return !account.getBalance().equals(account.withdraw(amount));
        })).orElse(false);
    }
}
