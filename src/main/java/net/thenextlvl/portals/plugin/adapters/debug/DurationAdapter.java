package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.time.Duration;

@NullMarked
public final class DurationAdapter implements JsonSerializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
        var millis = duration.toMillis();
        if (millis < 1000) return new JsonPrimitive(millis + " milliseconds");
        var seconds = duration.toSeconds();
        if (Math.abs(seconds) < 60) return new JsonPrimitive(seconds + " seconds");
        var minutes = duration.toMinutes();
        if (Math.abs(minutes) < 60) return new JsonPrimitive(minutes + " minutes");
        var hours = duration.toHours();
        if (Math.abs(hours) < 24) return new JsonPrimitive(hours + " hours");
        return new JsonPrimitive(duration.toDays() + " days");
    }
}
