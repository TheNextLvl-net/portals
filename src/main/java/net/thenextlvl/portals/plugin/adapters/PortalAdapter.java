package net.thenextlvl.portals.plugin.adapters;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.plugin.portal.PaperPortal;
import net.thenextlvl.portals.shape.BoundingBox;
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
        var boundingBox = context.deserialize(root.get("boundingBox"), BoundingBox.class);

        var portal = new PaperPortal(plugin, name, boundingBox);

        root.optional("cooldown").map(tag1 -> context.deserialize(tag1, Duration.class)).ifPresent(portal::setCooldown);
        root.optional("warmup").map(tag1 -> context.deserialize(tag1, Duration.class)).ifPresent(portal::setWarmup);
        root.optional("entryAction").map(tag1 -> context.deserialize(tag1, EntryAction.class)).ifPresent(portal::setEntryAction);
        root.optional("entryCost").map(tag1 -> context.deserialize(tag1, Double.class)).ifPresent(portal::setEntryCost);
        root.optional("entryPermission").map(tag1 -> context.deserialize(tag1, String.class)).ifPresent(portal::setEntryPermission);

        return portal;
    }

    @Override
    public Tag serialize(Portal portal, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.builder();

        tag.put("name", portal.getName());
        tag.put("boundingBox", context.serialize(portal.getBoundingBox(), BoundingBox.class));

        portal.getEntryAction().ifPresent(action -> tag.put("entryAction", context.serialize(action)));
        portal.getEntryPermission().ifPresent(permission -> tag.put("entryPermission", permission));

        tag.put("cooldown", context.serialize(portal.getCooldown()));
        tag.put("warmup", context.serialize(portal.getWarmup()));
        tag.put("entryCost", portal.getEntryCost());

        return tag.build();
    }
}
