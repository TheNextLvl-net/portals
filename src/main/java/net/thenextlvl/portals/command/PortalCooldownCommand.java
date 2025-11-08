package net.thenextlvl.portals.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import core.paper.command.argument.DurationArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

import static net.thenextlvl.portals.command.PortalCommand.portalArgument;

@NullMarked
final class PortalCooldownCommand extends SimpleCommand {
    public PortalCooldownCommand(PortalsPlugin plugin) {
        super(plugin, "cooldown", "portals.command.cooldown");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalCooldownCommand(plugin);
        var cooldown = Commands.argument("cooldown", DurationArgumentType.duration());
        return command.create().then(portalArgument(plugin)
                .then(cooldown.executes(command))
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();

        var portal = context.getArgument("portal", Portal.class);
        var cooldown = tryGetArgument(context, "cooldown", Duration.class).orElse(null);

        if (cooldown == null) {
            plugin.bundle().sendMessage(sender, "portal.cooldown.current",
                    Placeholder.parsed("portal", portal.getName()),
                    Formatter.number("cooldown", portal.getCooldown().toMillis() / 1000d));
            return SINGLE_SUCCESS;
        }

        var success = portal.setCooldown(cooldown);
        var message = success ? "portal.cooldown.set" : "nothing.changed";

        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("portal", portal.getName()),
                Formatter.number("cooldown", cooldown.toMillis() / 1000d));
        return success ? SINGLE_SUCCESS : 0;
    }
}