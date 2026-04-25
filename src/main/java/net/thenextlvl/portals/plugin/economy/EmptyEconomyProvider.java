package net.thenextlvl.portals.plugin.economy;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.stream.Stream;

@NullMarked
public final class EmptyEconomyProvider implements EconomyProvider {
    private final Plugin plugin;

    public EmptyEconomyProvider(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Component format(final Locale locale, @Nullable final String currency, final double amount) {
        final var format = String.format(locale, "%.2f", amount);
        if (currency == null) return Component.text(format);
        return Component.text(format).appendSpace().append(Component.text(currency));
    }

    @Override
    public boolean withdraw(final Player player, @Nullable final String currency, final double amount) {
        if (amount == 0) return true;
        plugin.getComponentLogger().warn("No economy provider installed, cannot withdraw money");
        return false;
    }

    @Override
    public boolean currencyExists(final String currency) {
        return false;
    }

    @Override
    public Stream<String> getCurrencies() {
        return Stream.empty();
    }
}
