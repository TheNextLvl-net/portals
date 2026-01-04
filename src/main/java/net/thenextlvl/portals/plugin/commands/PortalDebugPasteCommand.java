package net.thenextlvl.portals.plugin.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PortalDebugPasteCommand extends SimpleCommand {
    private final Gson gson = new GsonBuilder()
            .disableJdkUnsafe()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private PortalDebugPasteCommand(PortalsPlugin plugin) {
        super(plugin, "debug-paste", "portals.command.debug-paste");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalDebugPasteCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();

        var debug = new JsonObject();
        var portals = new JsonArray();
        plugin.portalProvider().portals.stream()
                .map(gson::toJsonTree)
                .forEach(portals::add);
        debug.add("config", gson.toJsonTree(plugin.config()));
        debug.add("portals", portals);

        plugin.bundle().sendMessage(sender, "portal.debug-paste",
                Placeholder.parsed("debug", gson.toJson(debug)));
        return SINGLE_SUCCESS;
    }
}
