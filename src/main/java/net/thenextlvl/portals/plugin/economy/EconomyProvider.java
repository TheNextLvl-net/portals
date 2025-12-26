package net.thenextlvl.portals.plugin.economy;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
public interface EconomyProvider {
    default String format(Audience audience, double amount) {
        return format(audience.get(Identity.LOCALE).orElse(Locale.US), amount);
    }

    default String format(Locale locale, double amount) {
        return String.format(locale, "%.2f", amount);
    }
    
    boolean withdraw(Player player, double amount);
}
