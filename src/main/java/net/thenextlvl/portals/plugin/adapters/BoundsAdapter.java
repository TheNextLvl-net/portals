package net.thenextlvl.portals.plugin.adapters;

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
    public Bounds deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var world = context.deserialize(root.get("world"), Key.class);
        final var min = context.deserialize(root.get("min"), BlockPosition.class);
        final var max = context.deserialize(root.get("max"), BlockPosition.class);
        return Bounds.factory().of(world, min, max);
    }

    @Override
    public Tag serialize(final Bounds bounds, final TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("world", context.serialize(bounds.worldKey(), Key.class))
                .put("min", context.serialize(bounds.minPosition()))
                .put("max", context.serialize(bounds.maxPosition()))
                .build();
    }
}
