package net.thenextlvl.portals.plugin.adapters;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.action.EntryAction;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.portal.PaperPortal;
import net.thenextlvl.portals.shape.BoundingBox;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public final class PortalAdapter implements TagAdapter<Portal> {
    private final PortalsPlugin plugin;

    public PortalAdapter(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Portal deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();

        final var name = root.get("name").getAsString();
        final var boundingBox = context.deserialize(root.get("boundingBox"), BoundingBox.class);

        final var portal = new PaperPortal(plugin, name, boundingBox);

        root.optional("cooldown").map(tag1 -> context.deserialize(tag1, Duration.class)).ifPresent(portal::setCooldown);
        root.optional("entryAction").map(tag1 -> context.deserialize(tag1, EntryAction.class)).ifPresent(portal::setEntryAction);
        root.optional("entryCost").map(tag1 -> context.deserialize(tag1, Double.class)).ifPresent(portal::setEntryCost);
        root.optional("entryPermission").map(tag1 -> context.deserialize(tag1, String.class)).ifPresent(portal::setEntryPermission);
        root.optional("warmup").map(tag1 -> context.deserialize(tag1, Duration.class)).ifPresent(portal::setWarmup);

        return portal;
    }

    @Override
    public Tag serialize(final Portal portal, final TagSerializationContext context) throws ParserException {
        final var tag = CompoundTag.builder();

        tag.put("name", portal.getName());
        tag.put("boundingBox", context.serialize(portal.getBoundingBox(), BoundingBox.class));

        portal.getEntryAction().ifPresent(action -> tag.put("entryAction", context.serialize(action)));
        portal.getEntryPermission().ifPresent(permission -> tag.put("entryPermission", permission));

        tag.put("cooldown", context.serialize(portal.getCooldown()));
        tag.put("entryCost", portal.getEntryCost());
        tag.put("warmup", context.serialize(portal.getWarmup()));

        return tag.build();
    }
}
