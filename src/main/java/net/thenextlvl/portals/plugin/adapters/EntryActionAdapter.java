package net.thenextlvl.portals.plugin.adapters;

import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.action.ActionTypeRegistry;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.bounds.Bounds;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EntryActionAdapter implements TagAdapter<EntryAction<?>> {
    private final NBT nbt;

    public EntryActionAdapter(final PortalsPlugin plugin) {
        this.nbt = NBT.builder()
                .registerTypeHierarchyAdapter(BlockPosition.class, new BlockPositionAdapter())
                .registerTypeHierarchyAdapter(Bounds.class, new BoundsAdapter())
                .registerTypeHierarchyAdapter(Key.class, new KeyAdapter())
                .registerTypeHierarchyAdapter(Location.class, new LazyLocationAdapter())
                .registerTypeHierarchyAdapter(PortalLike.class, new PortalLikeAdapter(plugin))
                .build();
    }

    @Override
    public EntryAction<?> deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var typeName = root.get("type").getAsString();
        final var actionType = ActionTypeRegistry.registry().getByName(typeName)
                .orElseThrow(() -> new ParserException("Unknown action type: " + typeName));
        final var input = nbt.deserialize(root.get("input"), actionType.getType());
        return EntryAction.create(actionType, input);
    }

    @Override
    public Tag serialize(final EntryAction<?> action, final TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("type", action.getActionType().getName())
                .put("input", nbt.serialize(action.getInput(), action.getActionType().getType()))
                .build();
    }
}
