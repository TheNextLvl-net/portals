package net.thenextlvl.portals.plugin.adapters;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.plugin.model.LazyLocation;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LazyLocationAdapter implements TagAdapter<Location> {
    @Override
    public Location deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var x = root.get("x").getAsDouble();
        var y = root.get("y").getAsDouble();
        var z = root.get("z").getAsDouble();
        var yaw = root.get("yaw").getAsFloat();
        var pitch = root.get("pitch").getAsFloat();
        var world = context.deserialize(root.get("world"), Key.class);
        return new LazyLocation(world, x, y, z, yaw, pitch);
    }

    @Override
    public Tag serialize(Location location, TagSerializationContext context) throws ParserException {
        var world = location instanceof LazyLocation lazy ? lazy.key() : location.getWorld().key();
        return CompoundTag.builder()
                .put("x", location.getX())
                .put("y", location.getY())
                .put("z", location.getZ())
                .put("yaw", location.getYaw())
                .put("pitch", location.getPitch())
                .put("world", context.serialize(world))
                .build();
    }
}
