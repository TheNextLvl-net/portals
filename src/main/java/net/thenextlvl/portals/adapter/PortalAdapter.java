package net.thenextlvl.portals.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.portal.PaperPortal;
import net.thenextlvl.portals.shape.Shape;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public final class PortalAdapter implements TagAdapter<Portal> {
    private final PortalsPlugin plugin;

    public PortalAdapter(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Portal deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();

        var name = root.get("name").getAsString();
        var boundingBox = context.deserialize(root.get("boundingBox"), Shape.class);

        var portal = new PaperPortal(plugin, name, boundingBox);

        root.optional("cooldown").map(tag1 -> context.deserialize(tag1, Duration.class)).ifPresent(portal::setCooldown);
        root.optional("entryAction").map(tag1 -> context.deserialize(tag1, EntryAction.class)).ifPresent(portal::setEntryAction);
        root.optional("entryCost").map(tag1 -> context.deserialize(tag1, Double.class)).ifPresent(portal::setEntryCost);
        root.optional("entryPermission").map(tag1 -> context.deserialize(tag1, String.class)).ifPresent(portal::setEntryPermission);

        return portal;
    }

    @Override
    public Tag serialize(Portal portal, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();

        tag.add("name", portal.getName());
        tag.add("boundingBox", context.serialize(portal.getBoundingBox(), Shape.class));

        portal.getEntryAction().ifPresent(action -> tag.add("entryAction", context.serialize(action)));
        portal.getEntryPermission().ifPresent(permission -> tag.add("entryPermission", permission));

        tag.add("cooldown", context.serialize(portal.getCooldown()));
        tag.add("entryCost", portal.getEntryCost());

        return tag;
    }
}
