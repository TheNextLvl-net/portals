package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

@NullMarked
public final class WorldAdapter implements JsonSerializer<World> {
    @Override
    public JsonElement serialize(World world, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(world.getName());
    }
}
