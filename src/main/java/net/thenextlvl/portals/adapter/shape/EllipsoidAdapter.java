package net.thenextlvl.portals.adapter.shape;

import io.papermc.paper.math.FinePosition;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.shape.BoundingBox;
import net.thenextlvl.portals.shape.Ellipsoid;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EllipsoidAdapter implements TagAdapter<Ellipsoid> {
    @Override
    public Ellipsoid deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var world = context.deserialize(root.get("world"), World.class);
        var center = context.deserialize(root.get("center"), FinePosition.class);
        var radius = root.get("radius").getAsDouble();
        var height = root.get("height").getAsDouble();
        return BoundingBox.ellipsoid(world, center, radius, height);
    }

    @Override
    public Tag serialize(Ellipsoid ellipsoid, TagSerializationContext context) throws ParserException {
        var root = CompoundTag.empty();
        root.add("world", context.serialize(ellipsoid.getWorld()));
        root.add("center", context.serialize(ellipsoid.getCenter(), FinePosition.class));
        root.add("radius", ellipsoid.getRadius());
        root.add("height", ellipsoid.getHeight());
        return root;
    }
}
