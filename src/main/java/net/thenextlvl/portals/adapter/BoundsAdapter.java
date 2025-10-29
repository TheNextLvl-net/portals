package net.thenextlvl.portals.adapter;

import io.papermc.paper.math.FinePosition;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.action.teleport.Bounds;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BoundsAdapter implements TagAdapter<Bounds> {
    @Override
    public Bounds deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var world = context.deserialize(root.get("world"), World.class);
        var min = context.deserialize(root.get("min"), FinePosition.class);
        var max = context.deserialize(root.get("max"), FinePosition.class);
        return new Bounds(world, min, max);
    }

    @Override
    public Tag serialize(Bounds bounds, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("world", context.serialize(bounds.world(), World.class));
        tag.add("min", context.serialize(bounds.minPosition(), FinePosition.class));
        tag.add("max", context.serialize(bounds.maxPosition(), FinePosition.class));
        return tag;
    }
}
