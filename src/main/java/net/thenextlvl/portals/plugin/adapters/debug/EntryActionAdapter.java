package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.thenextlvl.portals.action.EntryAction;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

@NullMarked
public final class EntryActionAdapter implements JsonSerializer<EntryAction<?>> {
    @Override
    public JsonElement serialize(EntryAction<?> action, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        var type = new JsonObject();

        type.addProperty("class", action.getActionType().getClass().getName());
        type.addProperty("name", action.getActionType().getName());

        object.add("type", type);
        object.add("input", context.serialize(action.getInput()));
        return object;
    }
}
