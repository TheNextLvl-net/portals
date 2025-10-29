package net.thenextlvl.portals.adapter.shape;

import io.papermc.paper.math.FinePosition;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.shape.BoundingBox;
import net.thenextlvl.portals.shape.Sphere;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SphereAdapter implements TagAdapter<Sphere> {
    @Override
    public Sphere deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var world = context.deserialize(root.get("world"), World.class);
        var center = context.deserialize(root.get("center"), FinePosition.class);
        var radius = root.get("radius").getAsDouble();
        return BoundingBox.sphere(world, center, radius);
    }

    @Override
    public Tag serialize(Sphere object, TagSerializationContext context) throws ParserException {
        var root = CompoundTag.empty();
        root.add("world", context.serialize(object.getWorld()));
        root.add("radius", object.getRadius());
        root.add("center", context.serialize(object.getCenter(), FinePosition.class));
        return root;
    }
}
