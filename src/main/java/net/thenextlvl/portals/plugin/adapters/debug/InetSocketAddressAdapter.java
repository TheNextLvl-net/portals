package net.thenextlvl.portals.plugin.adapters.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

@NullMarked
public final class InetSocketAddressAdapter implements JsonSerializer<InetSocketAddress> {
    @Override
    public JsonElement serialize(InetSocketAddress address, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(address.getHostString() + ":" + address.getPort());
    }
}
