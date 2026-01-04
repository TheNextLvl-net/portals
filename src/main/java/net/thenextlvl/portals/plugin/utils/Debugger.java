package net.thenextlvl.portals.plugin.utils;

import org.intellij.lang.annotations.PrintFormat;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.stream.Stream;

@NullMarked
public final class Debugger {
    private final int maxLogs = Integer.getInteger("portals.debug.max-logs", 250);
    private final long startTime = System.currentTimeMillis();
    private final ArrayDeque<String> logs = new ArrayDeque<>(maxLogs);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private int transaction = 1;
    public int omittedLogs = 0;

    public void newTransaction() {
        transaction++;
    }

    public void log(@PrintFormat String log, Object... args) {
        if (logs.size() >= maxLogs) {
            logs.removeFirst();
            omittedLogs++;
        }
        var time = formatter.format(Instant.now().atZone(ZoneId.systemDefault()));
        logs.add("[" + time + "] #" + transaction + " " + String.format(log, args));
    }

    public long uptime() {
        return System.currentTimeMillis() - startTime;
    }

    public Stream<String> logs() {
        return logs.stream();
    }

    public String durationToString(Duration duration) {
        var millis = duration.toMillis();
        if (millis < 1000) return millis + " milliseconds";
        var seconds = duration.toSeconds();
        if (Math.abs(seconds) < 60) return seconds + " seconds";
        var minutes = duration.toMinutes();
        if (Math.abs(minutes) < 60) return minutes + " minutes";
        var hours = duration.toHours();
        if (Math.abs(hours) < 24) return hours + " hours";
        return duration.toDays() + " days";
    }
}
