package net.thenextlvl.portals.plugin.adapters;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.effect.PortalEffect;
import net.thenextlvl.portals.effect.PortalEffectTypeRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PortalEffectAdapter implements TagAdapter<PortalEffect> {
    private final PortalEffectTypeRegistry registry;

    public PortalEffectAdapter(final PortalEffectTypeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public PortalEffect deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var typeName = root.get("type").getAsString();
        final var type = registry.getByName(typeName)
                .orElseThrow(() -> new ParserException("Unknown portal effect type: " + typeName));
        return type.deserialize(root.get("data").getAsCompound(), context);
    }

    @Override
    public Tag serialize(final PortalEffect effect, final TagSerializationContext context) throws ParserException {
        final var type = registry.getByEffect(effect)
                .orElseThrow(() -> new ParserException("Unsupported portal effect type: " + effect.getClass().getName()));
        return net.thenextlvl.nbt.tag.CompoundTag.builder()
                .put("type", type.getName())
                .put("data", serialize(type, effect, context))
                .build();
    }

    @SuppressWarnings("unchecked")
    private static <T extends PortalEffect, B extends PortalEffect.Builder<T, B>> Tag serialize(
            final net.thenextlvl.portals.effect.PortalEffectType<?, ?> type,
            final PortalEffect effect,
            final TagSerializationContext context
    ) throws ParserException {
        return ((net.thenextlvl.portals.effect.PortalEffectType<T, B>) type).serialize((T) effect, context);
    }
}
