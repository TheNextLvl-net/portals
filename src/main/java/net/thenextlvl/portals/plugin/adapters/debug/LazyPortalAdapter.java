package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.thenextlvl.portals.plugin.model.LazyPortal;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

@NullMarked
public final class LazyPortalAdapter implements JsonSerializer<LazyPortal> {
    @Override
    public JsonElement serialize(LazyPortal portal, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(portal.getName());
    }
}
