package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.thenextlvl.portals.plugin.model.LazyLocation;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

@NullMarked
public final class LocationAdapter implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        var key = location instanceof LazyLocation lazy ? lazy.key()
                : location.getWorld() != null ? location.getWorld().key() : null;
        object.add("world", context.serialize(key));
        object.addProperty("x", location.getX());
        object.addProperty("y", location.getY());
        object.addProperty("z", location.getZ());
        object.addProperty("yaw", location.getYaw());
        object.addProperty("pitch", location.getPitch());
        return object;
    }
}
