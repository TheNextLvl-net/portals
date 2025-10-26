package net.thenextlvl.portals.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import net.thenextlvl.portals.command.suggestion.PermissionSuggestionProvider;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static net.thenextlvl.portals.command.PortalCommand.portalArgument;

@NullMarked
public final class PortalPermissionCommand extends SimpleCommand {
    public PortalPermissionCommand(PortalsPlugin plugin) {
        super(plugin, "permission", "portals.command.permission");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalPermissionCommand(plugin);
        var permission = Commands.argument("permission", StringArgumentType.string())
                .suggests(new PermissionSuggestionProvider<>(plugin));
        return command.create().then(portalArgument(plugin)
                .then(Commands.literal("remove").executes(command::set))
                .then(permission.executes(command::set))
                .executes(command));
    }

    private int set(CommandContext<CommandSourceStack> context) {
        var portal = context.getArgument("portal", Portal.class);
        var permission = tryGetArgument(context, "permission", String.class).orElse(null);
        var success = !Objects.equals(portal.getEntryPermission().orElse(null), permission);
        if (success) portal.setEntryPermission(permission);
        var message = !success ? "nothing.changed" : permission != null
                ? "portal.permission.set" : "portal.permission.cleared";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("permission", String.valueOf(permission)));
        return success ? SINGLE_SUCCESS : 0;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var portal = context.getArgument("portal", Portal.class);
        var permission = portal.getEntryPermission().orElse(null);
        var message = permission != null ? "portal.permission" : "portal.permission.none";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("permission", String.valueOf(permission)));
        return SINGLE_SUCCESS;
    }
}