package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.nio.file.Path;

@NullMarked
public final class PathAdapter implements JsonSerializer<Path> {
    @Override
    public JsonElement serialize(Path path, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(path.toString());
    }
}
