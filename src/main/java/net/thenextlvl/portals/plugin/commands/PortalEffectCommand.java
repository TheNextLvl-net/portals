package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.effect.PortalEffectType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.CommandOptionsArgument;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class PortalEffectCommand extends SimpleCommand {
    private static final String OPTIONS = "options";

    private PortalEffectCommand(final PortalsPlugin plugin) {
        super(plugin, "effect", "portals.command.effect");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalEffectCommand(plugin);
        final var portal = portalArgument(plugin)
                .then(Commands.literal("clear").executes(command::clear))
                .executes(command);

        plugin.portalEffectTypeRegistry().getTypes().forEach(type -> portal.then(command.createType(type)));
        return command.create().then(portal);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var portal = context.getArgument("portal", Portal.class);
        final var type = portal.getPortalEffect()
                .flatMap(plugin.portalEffectTypeRegistry()::getByEffect)
                .map(PortalEffectType::getName)
                .orElse(null);
        if (type == null) {
            plugin.bundle().sendMessage(sender, "portal.effect.none", Placeholder.parsed("portal", portal.getName()));
            return SINGLE_SUCCESS;
        }
        plugin.bundle().sendMessage(sender, "portal.effect.current",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("type", type));
        return SINGLE_SUCCESS;
    }

    private int clear(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var portal = context.getArgument("portal", Portal.class);
        final var success = portal.setPortalEffect(null);
        plugin.bundle().sendMessage(sender, success ? "portal.effect.cleared" : "nothing.changed",
                Placeholder.parsed("portal", portal.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }

    private LiteralArgumentBuilder<CommandSourceStack> createType(final PortalEffectType<?, ?> type) {
        final var options = Commands.argument(OPTIONS, new CommandOptionsArgument(type.getOptions()));
        return Commands.literal(type.getName())
                .executes(context -> set(context, type))
                .then(options.executes(context -> set(context, type)));
    }

    private int set(final CommandContext<CommandSourceStack> context, final PortalEffectType<?, ?> type) {
        final var sender = context.getSource().getSender();
        final var portal = context.getArgument("portal", Portal.class);
        final var options = tryGetArgument(context, OPTIONS, CommandOptionsArgument.Options.class)
                .orElseGet(CommandOptionsArgument.Options::new);
        final var effect = type.create(new ParsedCommandInput(options));
        final var success = portal.setPortalEffect(effect);
        plugin.bundle().sendMessage(sender, success ? "portal.effect.set" : "nothing.changed",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("type", type.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }

    private record ParsedCommandInput(CommandOptionsArgument.Options options) implements PortalEffectType.CommandInput {
        @Override
        public <T> Optional<T> get(final String option, final Class<T> type) {
            return options.getArgument(option, type);
        }
    }
}
