package net.thenextlvl.portals.plugin.utils;

import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.intellij.lang.annotations.PrintFormat;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class Debugger {
    private final int maxLogs = Integer.getInteger("portals.debug.max-logs", 250);
    private final long startTime = System.currentTimeMillis();
    private final Deque<String> logs = new LinkedBlockingDeque<>(maxLogs);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final AtomicBoolean broadcast = new AtomicBoolean(true);
    private final AtomicInteger omittedLogs = new AtomicInteger();
    private final AtomicInteger transaction = new AtomicInteger();

    private final NBT nbt = NBT.builder()
            .setPrettyPrinting(true)
            .setIndents(2)
            .build();

    private final PortalsPlugin plugin;

    public Debugger(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean getBroadcast() {
        return broadcast.get();
    }

    public void setBroadcast(final boolean broadcast) {
        this.broadcast.set(broadcast);
    }

    public Transaction newTransaction() {
        return new Transaction(transaction.incrementAndGet());
    }

    public final class Transaction {
        private final int id;

        public Transaction(final int id) {
            this.id = id;
        }

        public void log(@PrintFormat final String log, final Object... args) {
            final var message = String.format(log, args);
            final var time = formatter.format(Instant.now().atZone(ZoneId.systemDefault()));
            final var entry = "[" + time + "] #" + id + " " + message;

            if (getBroadcast()) broadcast(message);

            while (!logs.offer(entry)) {
                logs.pollFirst();
                omittedLogs.incrementAndGet();
            }
        }

        private void broadcast(final String message) {
            final var placeholder1 = Placeholder.parsed("message", message);
            final var placeholder2 = Formatter.number("id", id);
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                if (!player.hasPermission("portals.debug")) return;
                plugin.bundle().sendMessage(player, "portal.debug", placeholder1, placeholder2);
            });
        }
    }

    public String buildPaste() {
        final var debug = CompoundTag.builder();
        final var portals = ListTag.builder().contentType(CompoundTag.ID);
        final var logs = ListTag.builder().contentType(StringTag.ID);
        final var errors = ListTag.builder().contentType(StringTag.ID);

        debug.put("version", plugin.getPluginMeta().getVersion());
        debug.put("server", plugin.getServer().getName() + " " + plugin.getServer().getVersion());
        debug.put("uptime", durationToString(Duration.ofMillis(System.currentTimeMillis() - startTime)));

        final var omitted = omittedLogs.get();
        if (omitted > 0) logs.add(StringTag.of("Omitted " + omitted + " logs"));

        plugin.portalProvider().portals.stream().map(portal -> {
            try {
                final var nbt = plugin.nbt(portal.getWorld());
                return CompoundTag.builder()
                        .put("world", nbt.serialize(portal.getWorld().getKey()))
                        .putAll(nbt.serialize(portal).getAsCompound())
                        .build();
            } catch (final Exception e) {
                plugin.getComponentLogger().warn("Failed to serialize portal {}", portal.getName(), e);
                plugin.getComponentLogger().warn("Please look for similar issues or report this on GitHub: {}", ISSUES);
                errors.add(StringTag.of("Failed to serialize portal " + portal.getName() + ": " + e.getMessage()));
                PortalsPlugin.ERROR_TRACKER.trackError(e);
                return null;
            }
        }).filter(Objects::nonNull).forEach(portals::add);
        this.logs.stream().map(StringTag::of).forEach(logs::add);

        debug.put("config", CompoundTag.builder()
                .put("allowCaveSpawns", plugin.config().allowCaveSpawns())
                .put("entryCosts", plugin.config().entryCosts())
                .put("ignoreEntityMovement", plugin.config().ignoreEntityMovement())
                .put("pushbackSpeed", plugin.config().pushbackSpeed())
                .build());

        if (!portals.isEmpty()) debug.put("portals", portals.build());
        if (!logs.isEmpty()) debug.put("logs", logs.build());
        if (!errors.isEmpty()) debug.put("errors", errors.build());

        return nbt.toString(debug.build());
    }

    public CompletableFuture<String> uploadPaste(final String content) {
        try (final var client = HttpClient.newHttpClient()) {
            final var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.pastes.dev/post"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(content))
                    .timeout(Duration.ofSeconds(5))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                if (response.statusCode() == 201) {
                    final var trim = response.body().trim();
                    return "https://pastes.dev/" + trim.substring(8, trim.length() - 2);
                }
                throw new RuntimeException("Failed to upload paste (" + response.statusCode() + "): " + response.body());
            }).orTimeout(5, TimeUnit.SECONDS);
        }
    }

    public static String durationToString(final Duration duration) {
        final var millis = duration.toMillis();
        if (millis < 1000) return millis + " milliseconds";
        final var seconds = duration.toSeconds();
        if (Math.abs(seconds) < 60) return seconds + " seconds";
        final var minutes = duration.toMinutes();
        if (Math.abs(minutes) < 60) return minutes + " minutes";
        final var hours = duration.toHours();
        if (Math.abs(hours) < 24) return hours + " hours";
        return duration.toDays() + " days";
    }
}
