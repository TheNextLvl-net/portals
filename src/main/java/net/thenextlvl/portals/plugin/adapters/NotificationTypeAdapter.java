package net.thenextlvl.portals.plugin.adapters;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.notification.NotificationTypeRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class NotificationTypeAdapter implements TagAdapter<NotificationType<?>> {
    @Override
    public NotificationType<?> deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        return NotificationTypeRegistry.registry().getByName(tag.getAsString()).orElseThrow(() -> {
            return new ParserException("Unknown notification type: " + tag.getAsString());
        });
    }

    @Override
    public Tag serialize(final NotificationType<?> type, final TagSerializationContext context) throws ParserException {
        return StringTag.of(type.getName());
    }
}
