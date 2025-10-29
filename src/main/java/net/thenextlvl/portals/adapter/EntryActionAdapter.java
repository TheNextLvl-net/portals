package net.thenextlvl.portals.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.action.ActionTypeRegistry;
import net.thenextlvl.portals.action.EntryAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EntryActionAdapter implements TagAdapter<EntryAction<?>> {
    @Override
    public EntryAction<?> deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var typeName = root.get("type").getAsString();
        var actionType = ActionTypeRegistry.registry().getByName(typeName)
                .orElseThrow(() -> new ParserException("Unknown action type: " + typeName));
        // fixme: cant deserialize portal arguments
        var input = context.deserialize(root.get("input"), actionType.type());
        return EntryAction.create(actionType, input);
    }

    @Override
    public Tag serialize(EntryAction<?> action, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("type", action.getType().name());
        // fixme: cant serialize portal arguments
        tag.add("input", context.serialize(action.getInput(), action.getType().type()));
        return tag;
    }
}
