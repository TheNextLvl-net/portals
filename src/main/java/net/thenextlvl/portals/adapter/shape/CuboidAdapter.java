package net.thenextlvl.portals.adapter.shape;

import io.papermc.paper.math.FinePosition;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.shape.BoundingBox;
import net.thenextlvl.portals.shape.Cuboid;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CuboidAdapter implements TagAdapter<Cuboid> {
    @Override
    public Cuboid deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var world = context.deserialize(root.get("world"), World.class);
        var min = context.deserialize(root.get("min"), FinePosition.class);
        var max = context.deserialize(root.get("max"), FinePosition.class);
        return BoundingBox.cuboid(world, min, max);
    }

    @Override
    public Tag serialize(Cuboid cuboid, TagSerializationContext context) throws ParserException {
        var root = CompoundTag.empty();
        root.add("world", context.serialize(cuboid.getWorld()));
        root.add("min", context.serialize(cuboid.getMinPosition()));
        root.add("max", context.serialize(cuboid.getMaxPosition()));
        return root;
    }
}
