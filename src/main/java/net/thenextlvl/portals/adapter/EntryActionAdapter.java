package net.thenextlvl.portals.adapter;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.ActionTypeRegistry;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.action.teleport.Bounds;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EntryActionAdapter implements TagAdapter<EntryAction<?>> {
    private final NBT nbt;

    public EntryActionAdapter(PortalsPlugin plugin) {
        this.nbt = NBT.builder()
                .registerTypeHierarchyAdapter(Bounds.class, new BoundsAdapter())
                .registerTypeHierarchyAdapter(Key.class, new KeyAdapter())
                .registerTypeHierarchyAdapter(Location.class, new LazyLocationAdapter())
                .registerTypeHierarchyAdapter(Portal.class, new LazyPortalAdapter(plugin))
                .build();
    }

    @Override
    public EntryAction<?> deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var typeName = root.get("type").getAsString();
        var actionType = ActionTypeRegistry.registry().getByName(typeName)
                .orElseThrow(() -> new ParserException("Unknown action type: " + typeName));
        var input = nbt.deserialize(root.get("input"), actionType.getType());
        return EntryAction.create(actionType, input);
    }

    @Override
    public Tag serialize(EntryAction<?> action, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("type", action.getActionType().getName());
        tag.add("input", nbt.serialize(action.getInput(), action.getActionType().getType()));
        return tag;
    }
}
