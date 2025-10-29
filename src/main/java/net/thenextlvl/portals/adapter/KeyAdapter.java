package net.thenextlvl.portals.adapter;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class KeyAdapter implements TagAdapter<Key> {
    @Override
    @SuppressWarnings("PatternValidation")
    public Key deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return Key.key(tag.getAsString());
    }

    @Override
    public Tag serialize(Key key, TagSerializationContext context) throws ParserException {
        return StringTag.of(key.asString());
    }
}
