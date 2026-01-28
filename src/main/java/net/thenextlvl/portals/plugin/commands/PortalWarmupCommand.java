package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.DurationArgumentType;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class PortalWarmupCommand extends SimpleCommand {
    public PortalWarmupCommand(PortalsPlugin plugin) {
        super(plugin, "warmup", "portals.command.warmup");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalWarmupCommand(plugin);
        var warmup = Commands.argument("warmup", DurationArgumentType.duration());
        return command.create().then(portalArgument(plugin)
                .then(warmup.executes(command))
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();

        var portal = context.getArgument("portal", Portal.class);
        var warmup = tryGetArgument(context, "warmup", Duration.class).orElse(null);

        if (warmup == null) {
            plugin.bundle().sendMessage(sender, "portal.warmup.current",
                    Placeholder.parsed("portal", portal.getName()),
                    Formatter.number("warmup", portal.getWarmup().toMillis() / 1000d));
            return SINGLE_SUCCESS;
        }

        var success = portal.setWarmup(warmup);
        var message = success ? "portal.warmup.set" : "nothing.changed";

        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("portal", portal.getName()),
                Formatter.number("warmup", warmup.toMillis() / 1000d));
        return success ? SINGLE_SUCCESS : 0;
    }
}

