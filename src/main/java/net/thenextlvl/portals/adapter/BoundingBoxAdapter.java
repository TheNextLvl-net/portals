package net.thenextlvl.portals.adapter;

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

    public BoundingBoxAdapter(World world) {
        this.world = world;
    }

    @Override
    public BoundingBox deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var min = context.deserialize(root.get("min"), Position.class);
        var max = context.deserialize(root.get("max"), Position.class);
        return BoundingBox.of(world, min, max);
    }

    @Override
    public Tag serialize(BoundingBox boundingBox, TagSerializationContext context) throws ParserException {
        var root = CompoundTag.empty();
        root.add("min", context.serialize(boundingBox.getMinPosition()));
        root.add("max", context.serialize(boundingBox.getMaxPosition()));
        return root;
    }
}
