package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.thenextlvl.portals.PortalLike;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

@NullMarked
public final class PortalLikeAdapter implements JsonSerializer<PortalLike> {
    @Override
    public JsonElement serialize(PortalLike portal, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(portal.getName());
    }
}
