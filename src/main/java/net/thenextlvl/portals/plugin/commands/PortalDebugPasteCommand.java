package net.thenextlvl.portals.plugin.commands;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.adapters.debug.DurationAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.EntryActionAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.PortalAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.WorldAdapter;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import net.thenextlvl.portals.plugin.portal.PaperPortal;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.Objects;

@NullMarked
final class PortalDebugPasteCommand extends SimpleCommand {
    private PortalDebugPasteCommand(PortalsPlugin plugin) {
        super(plugin, "debug-paste", "portals.command.debug-paste");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PortalsPlugin plugin) {
        var command = new PortalDebugPasteCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    protected boolean canUse(CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();

        var debug = new JsonObject();
        var logs = new JsonArray();
        var portals = new JsonArray();
        var debugs = new JsonArray();

        var exclusionStrategy = new DebugExclusionStrategy(debugs);
        var gson = new GsonBuilder()
                .registerTypeAdapter(PaperPortal.class, new PortalAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeHierarchyAdapter(EntryAction.class, new EntryActionAdapter(exclusionStrategy))
                .registerTypeHierarchyAdapter(World.class, new WorldAdapter())
                .addSerializationExclusionStrategy(exclusionStrategy)
                .disableHtmlEscaping()
                .disableJdkUnsafe()
                .setPrettyPrinting()
                .create();

        debug.addProperty("version", plugin.getPluginMeta().getVersion());
        debug.addProperty("server", plugin.getServer().getName() + " " + plugin.getServer().getVersion());
        debug.addProperty("uptime", DurationAdapter.toString(Duration.ofMillis(plugin.uptime())));

        if (plugin.omittedLogs() > 0) logs.add(new JsonPrimitive(
                "Omitted " + plugin.omittedLogs() + " logs"
        ));

        plugin.portalProvider().portals.stream().map(portal -> {
            try {
                return gson.toJsonTree(portal);
            } catch (Exception e) {
                plugin.getComponentLogger().warn("Failed to serialize portal {}", portal.getName(), e);
                debugs.add("Failed to serialize portal " + portal.getName() + ": " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).forEach(portals::add);
        plugin.logs().map(JsonPrimitive::new).forEach(logs::add);

        try {
            debug.add("config", gson.toJsonTree(plugin.config()));
        } catch (Exception e) {
            plugin.getComponentLogger().warn("Failed to serialize config", e);
            debugs.add("Failed to serialize config: " + e.getMessage());
        }

        debug.add("portals", portals);
        debug.add("logs", logs);
        debug.add("debugs", debugs);

        plugin.bundle().sendMessage(sender, "portal.debug-paste",
                Placeholder.parsed("debug", gson.toJson(debug)));
        return SINGLE_SUCCESS;
    }

    private record DebugExclusionStrategy(JsonArray debugs) implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            if (f.getDeclaredClass().isPrimitive() || f.getDeclaredClass().isEnum()) return false;
            debugs.add("Processing field (" + f.getDeclaredClass().getName() + ") " + f.getDeclaringClass().getName() + "#" + f.getName());
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz.equals(PortalsPlugin.class);
        }
    }
}
