package net.thenextlvl.portals.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.model.LazyPortal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalLikeAdapter implements TagAdapter<PortalLike> {
    private final PortalsPlugin plugin;

    public PortalLikeAdapter(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PortalLike deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var name = tag.getAsString();
        return plugin.portalProvider().getPortal(name)
                .<PortalLike>map(portal -> portal)
                .orElseGet(() -> new LazyPortal(name));
    }

    @Override
    public Tag serialize(PortalLike portal, TagSerializationContext context) throws ParserException {
        return StringTag.of(portal.getName());
    }
}
