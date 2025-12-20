package net.thenextlvl.portals.adapter;

import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.bounds.Bounds;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BoundsAdapter implements TagAdapter<Bounds> {
    @Override
    public Bounds deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var world = context.deserialize(root.get("world"), Key.class);
        var min = context.deserialize(root.get("min"), BlockPosition.class);
        var max = context.deserialize(root.get("max"), BlockPosition.class);
        return Bounds.factory().of(world, min, max);
    }

    @Override
    public Tag serialize(Bounds bounds, TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("world", context.serialize(bounds.worldKey(), Key.class))
                .put("min", context.serialize(bounds.minPosition()))
                .put("max", context.serialize(bounds.maxPosition()))
                .build();
    }
}
