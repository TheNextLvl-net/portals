package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.action.EntryAction;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

@NullMarked
public final class EntryActionAdapter implements JsonSerializer<EntryAction<?>> {
    private final Gson gson;

    public EntryActionAdapter(ExclusionStrategy exclusionStrategy) {
        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(World.class, new WorldAdapter())
                .registerTypeHierarchyAdapter(PortalLike.class, new PortalLikeAdapter())
                .registerTypeHierarchyAdapter(InetSocketAddress.class, new InetSocketAddressAdapter())
                .addSerializationExclusionStrategy(exclusionStrategy)
                .disableJdkUnsafe()
                .create();
    }

    @Override
    public JsonElement serialize(EntryAction<?> action, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("type", action.getActionType().getName());
        object.add("input", gson.toJsonTree(action.getInput()));
        return object;
    }
}
