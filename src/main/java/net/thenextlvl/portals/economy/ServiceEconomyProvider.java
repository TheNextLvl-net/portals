package net.thenextlvl.portals.economy;

import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.service.api.economy.EconomyController;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Optional;

@NullMarked
public final class ServiceEconomyProvider implements EconomyProvider {
    private final PortalsPlugin plugin;

    public ServiceEconomyProvider(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    private Optional<EconomyController> getController() {
        return Optional.ofNullable(plugin.getServer().getServicesManager().load(EconomyController.class));
    }

    @Override
    public String format(Locale locale, double amount) {
        return getController().map(controller -> controller.format(amount))
                .orElseGet(() -> EconomyProvider.super.format(locale, amount));
    }

    @Override
    public boolean withdraw(Player player, double amount) {
        return getController().flatMap(controller -> controller.getAccount(player).map(account -> {
            return !account.getBalance().equals(account.withdraw(amount));
        })).orElse(false);
    }
}
