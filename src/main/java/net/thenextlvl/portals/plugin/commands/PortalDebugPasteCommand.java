package net.thenextlvl.portals.plugin.commands;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.adapters.debug.DurationAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.EntryActionAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.InetSocketAddressAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.LazyPortalAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.PathAdapter;
import net.thenextlvl.portals.plugin.adapters.debug.WorldAdapter;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import net.thenextlvl.portals.plugin.model.LazyPortal;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.time.Duration;

@NullMarked
final class PortalDebugPasteCommand extends SimpleCommand {
    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Path.class, new PathAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(EntryAction.class, new EntryActionAdapter())
            .registerTypeHierarchyAdapter(World.class, new WorldAdapter())
            .registerTypeAdapter(LazyPortal.class, new LazyPortalAdapter())
            .registerTypeHierarchyAdapter(InetSocketAddress.class, new InetSocketAddressAdapter())
            .addSerializationExclusionStrategy(new DebugExclusionStrategy())
            .disableHtmlEscaping()
            .disableJdkUnsafe()
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
    protected boolean canUse(CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
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

    private static final class DebugExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz.equals(PortalsPlugin.class);
        }
    }
}
