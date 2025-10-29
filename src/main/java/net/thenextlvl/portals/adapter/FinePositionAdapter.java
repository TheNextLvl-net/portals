package net.thenextlvl.portals.adapter;

import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class FinePositionAdapter implements TagAdapter<FinePosition> {
    @Override
    public FinePosition deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var x = root.get("x").getAsDouble();
        var y = root.get("y").getAsDouble();
        var z = root.get("z").getAsDouble();
        return Position.fine(x, y, z);
    }

    @Override
    public Tag serialize(FinePosition position, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("x", position.x());
        tag.add("y", position.y());
        tag.add("z", position.z());
        return tag;
    }
}
