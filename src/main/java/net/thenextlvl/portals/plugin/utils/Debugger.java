package net.thenextlvl.portals.plugin.utils;

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
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static net.thenextlvl.portals.plugin.PortalsPlugin.ISSUES;

@NullMarked
public final class Debugger {
    private final int maxLogs = Integer.getInteger("portals.debug.max-logs", 250);
    private final long startTime = System.currentTimeMillis();
    private final ArrayDeque<String> logs = new ArrayDeque<>(maxLogs);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private int transaction = 1;
    public int omittedLogs = 0;

    private final NBT nbt = NBT.builder()
            .setPrettyPrinting(true)
            .setIndents(2)
            .build();

    private final PortalsPlugin plugin;

    public Debugger(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    public void newTransaction() {
        transaction++;
    }

    public void log(@PrintFormat final String log, final Object... args) {
        if (logs.size() >= maxLogs) {
            logs.removeFirst();
            omittedLogs++;
        }
        final var time = formatter.format(Instant.now().atZone(ZoneId.systemDefault()));
        logs.add("[" + time + "] #" + transaction + " " + String.format(log, args));
    }

    public Stream<String> logs() {
        return logs.stream();
    }

    public String durationToString(final Duration duration) {
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

    public String buildPaste() {
        final var debug = CompoundTag.builder();
        final var portals = ListTag.builder().contentType(CompoundTag.ID);
        final var logs = ListTag.builder().contentType(StringTag.ID);
        final var errors = ListTag.builder().contentType(StringTag.ID);

        debug.put("version", plugin.getPluginMeta().getVersion());
        debug.put("server", plugin.getServer().getName() + " " + plugin.getServer().getVersion());
        debug.put("uptime", durationToString(Duration.ofMillis(System.currentTimeMillis() - startTime)));

        if (omittedLogs > 0) logs.add(StringTag.of("Omitted " + omittedLogs + " logs"));

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
        logs().map(StringTag::of).forEach(logs::add);

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
}
