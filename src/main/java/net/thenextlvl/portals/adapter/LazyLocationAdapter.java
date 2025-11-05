package net.thenextlvl.portals.adapter;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.lazy.LazyLocation;
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
        var tag = CompoundTag.empty();
        tag.add("x", location.getX());
        tag.add("y", location.getY());
        tag.add("z", location.getZ());
        tag.add("yaw", location.getYaw());
        tag.add("pitch", location.getPitch());
        var world = location instanceof LazyLocation lazy ? lazy.key() : location.getWorld().key();
        tag.add("world", context.serialize(world));
        return tag;
    }
}
