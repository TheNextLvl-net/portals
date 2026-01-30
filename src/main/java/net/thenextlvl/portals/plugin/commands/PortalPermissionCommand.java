package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import net.thenextlvl.portals.plugin.commands.suggestions.PermissionSuggestionProvider;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.portals.plugin.commands.PortalCommand.portalArgument;

@NullMarked
final class PortalPermissionCommand extends SimpleCommand {
    public PortalPermissionCommand(final PortalsPlugin plugin) {
        super(plugin, "permission", "portals.command.permission");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PortalPermissionCommand(plugin);
        final var permission = Commands.argument("permission", StringArgumentType.string())
                .suggests(new PermissionSuggestionProvider<>(plugin));
        return command.create().then(portalArgument(plugin)
                .then(Commands.literal("remove").executes(command::set))
                .then(permission.executes(command::set))
                .executes(command));
    }

    private int set(final CommandContext<CommandSourceStack> context) {
        final var portal = context.getArgument("portal", Portal.class);
        final var permission = tryGetArgument(context, "permission", String.class).orElse(null);

        final var success = portal.setEntryPermission(permission);
        final var message = !success ? "nothing.changed" : permission != null
                ? "portal.permission.set" : "portal.permission.cleared";

        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("permission", String.valueOf(permission)));
        return success ? SINGLE_SUCCESS : 0;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var portal = context.getArgument("portal", Portal.class);

        final var permission = portal.getEntryPermission().orElse(null);
        final var message = permission != null ? "portal.permission" : "portal.permission.none";

        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("permission", String.valueOf(permission)));
        return SINGLE_SUCCESS;
    }
}