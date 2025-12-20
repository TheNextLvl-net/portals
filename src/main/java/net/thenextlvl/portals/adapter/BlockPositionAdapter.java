package net.thenextlvl.portals.adapter;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BlockPositionAdapter implements TagAdapter<BlockPosition> {
    @Override
    public BlockPosition deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var x = root.get("x").getAsInt();
        var y = root.get("y").getAsInt();
        var z = root.get("z").getAsInt();
        return Position.block(x, y, z);
    }

    @Override
    public Tag serialize(BlockPosition position, TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("x", position.blockX())
                .put("y", position.blockY())
                .put("z", position.blockZ())
                .build();
    }
}
