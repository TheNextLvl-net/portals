package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypes;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class RunPlayerCommand extends StringActionCommand {
    private RunPlayerCommand(PortalsPlugin plugin) {
        super(plugin, ActionTypes.types().runCommand(), "run-command", "command");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        return new RunPlayerCommand(plugin).create();
    }

    @Override
    protected void onSuccess(CommandContext<CommandSourceStack> context, Portal portal, String input) {
        plugin.bundle().sendMessage(context.getSource().getSender(), "portal.action.run-command",
                Placeholder.parsed("portal", portal.getName()),
                Placeholder.parsed("command", input));
    }
}
