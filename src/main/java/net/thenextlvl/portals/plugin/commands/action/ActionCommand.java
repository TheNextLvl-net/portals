package net.thenextlvl.portals.plugin.commands.action;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.action.ActionType;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class ActionCommand<T> extends SimpleCommand {
    private final ActionType<T> actionType;

    protected ActionCommand(final PortalsPlugin plugin, final ActionType<T> actionType, final String name) {
        super(plugin, name, null);
        this.actionType = actionType;
    }

    protected int addAction(final CommandContext<CommandSourceStack> context, final T input) {
        final var sender = context.getSource().getSender();
        final var portal = context.getArgument("portal", Portal.class);

        final var success = !portal.getEntryAction().map(action -> {
            return action.getActionType().equals(actionType) && action.getInput().equals(input);
        }).orElse(false);

        if (!success) {
            plugin.bundle().sendMessage(sender, "nothing.changed");
            return 0;
        }

        portal.setEntryAction(EntryAction.create(actionType, input));
        onSuccess(context, portal, input);
        return SINGLE_SUCCESS;
    }

    protected abstract void onSuccess(CommandContext<CommandSourceStack> context, Portal portal, T input);
}
