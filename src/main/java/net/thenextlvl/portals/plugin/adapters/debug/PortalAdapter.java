package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.thenextlvl.portals.plugin.portal.PaperPortal;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

@NullMarked
public final class PortalAdapter implements JsonSerializer<PaperPortal> {
    @Override
    public JsonElement serialize(PaperPortal portal, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("name", portal.getName());
        object.add("boundingBox", context.serialize(portal.getBoundingBox()));
        object.add("cooldown", context.serialize(portal.getCooldown()));
        portal.getEntryAction().ifPresent(action -> object.add("entryAction", context.serialize(action)));
        object.addProperty("entryCost", portal.getEntryCost());
        object.addProperty("persistent", portal.isPersistent());
        object.addProperty("dataFile", portal.getDataFile().toString());
        object.addProperty("backupFile", portal.getBackupFile().toString());
        portal.getEntryPermission().ifPresent(permission -> object.addProperty("entryPermission", permission));
        return object;
    }
}
