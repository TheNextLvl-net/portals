package net.thenextlvl.portals.plugin.adapters;

import io.papermc.paper.math.Position;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BoundingBoxAdapter implements TagAdapter<BoundingBox> {
    private final World world;

    public BoundingBoxAdapter(final World world) {
        this.world = world;
    }

    @Override
    public BoundingBox deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var min = context.deserialize(root.get("min"), Position.class);
        final var max = context.deserialize(root.get("max"), Position.class);
        return BoundingBox.of(world, min, max);
    }

    @Override
    public Tag serialize(final BoundingBox boundingBox, final TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("min", context.serialize(boundingBox.getMinPosition()))
                .put("max", context.serialize(boundingBox.getMaxPosition()))
                .build();
    }
}
