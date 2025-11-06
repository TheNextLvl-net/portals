package net.thenextlvl.portals.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.lazy.LazyPortal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LazyPortalAdapter implements TagAdapter<Portal> {
    private final PortalsPlugin plugin;

    public LazyPortalAdapter(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Portal deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var name = tag.getAsString();
        return plugin.portalProvider().getPortal(name)
                .orElseGet(() -> new LazyPortal(name));
    }

    @Override
    public Tag serialize(Portal portal, TagSerializationContext context) throws ParserException {
        return StringTag.of(portal.getName());
    }
}
