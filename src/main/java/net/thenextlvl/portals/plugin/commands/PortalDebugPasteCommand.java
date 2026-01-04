package net.thenextlvl.portals.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.Objects;

@NullMarked
final class PortalDebugPasteCommand extends SimpleCommand {
    private final NBT nbt = NBT.builder()
            .setPrettyPrinting(true)
            .setIndents(2)
            .build();

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
        var debugger = plugin.debugger;

        var debug = CompoundTag.builder();
        var logs = ListTag.builder().contentType(StringTag.ID);
        var portals = ListTag.builder().contentType(CompoundTag.ID);
        var debugs = ListTag.builder().contentType(StringTag.ID);

        debug.put("version", plugin.getPluginMeta().getVersion());
        debug.put("server", plugin.getServer().getName() + " " + plugin.getServer().getVersion());
        debug.put("uptime", debugger.durationToString(Duration.ofMillis(debugger.uptime())));

        if (debugger.omittedLogs > 0) logs.add(StringTag.of("Omitted " + debugger.omittedLogs + " logs"));

        plugin.portalProvider().portals.stream().map(portal -> {
            try {
                return plugin.nbt(portal.getWorld()).serialize(portal);
            } catch (Exception e) {
                plugin.getComponentLogger().warn("Failed to serialize portal {}", portal.getName(), e);
                debugs.add(StringTag.of("Failed to serialize portal " + portal.getName() + ": " + e.getMessage()));
                return null;
            }
        }).filter(Objects::nonNull).forEach(portals::add);
        debugger.logs().map(StringTag::of).forEach(logs::add);

        debug.put("config", CompoundTag.builder()
                .put("allowCaveSpawns", plugin.config().allowCaveSpawns())
                .put("entryCosts", plugin.config().entryCosts())
                .put("ignoreEntityMovement", plugin.config().ignoreEntityMovement())
                .put("pushbackSpeed", plugin.config().pushbackSpeed())
                .build());

        debug.put("portals", portals.build());
        debug.put("logs", logs.build());
        debug.put("debugs", debugs.build());

        plugin.bundle().sendMessage(sender, "portal.debug-paste",
                Placeholder.parsed("debug", nbt.toString(debug.build())));
        return SINGLE_SUCCESS;
    }
}
