package net.thenextlvl.portals.command.action;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionType;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class ActionCommand<T> extends SimpleCommand {
    private final ActionType<T> actionType;

    protected ActionCommand(PortalsPlugin plugin, ActionType<T> actionType, String name) {
        super(plugin, name, null);
        this.actionType = actionType;
    }

    protected int addAction(CommandContext<CommandSourceStack> context, T input) {
        var sender = context.getSource().getSender();
        var portal = context.getArgument("portal", Portal.class);

        var success = !portal.getEntryAction().map(action -> {
            return action.getActionType().equals(actionType) && action.getInput().equals(input);
        }).orElse(false);
        if (success) portal.setEntryAction(EntryAction.create(actionType, input));

        var message = success ? "portal.action.set" : "nothing.changed";
        plugin.bundle().sendMessage(sender, message,
                Placeholder.unparsed("portal", portal.getName()),
                Placeholder.unparsed("action", actionType.name()));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
