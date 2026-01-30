package net.thenextlvl.portals.plugin.adapters;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.PortalLike;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.model.LazyPortal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalLikeAdapter implements TagAdapter<PortalLike> {
    private final PortalsPlugin plugin;

    public PortalLikeAdapter(final PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PortalLike deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var name = tag.getAsString();
        return plugin.portalProvider().getPortal(name)
                .<PortalLike>map(portal -> portal)
                .orElseGet(() -> new LazyPortal(name));
    }

    @Override
    public Tag serialize(final PortalLike portal, final TagSerializationContext context) throws ParserException {
        return StringTag.of(portal.getName());
    }
}
