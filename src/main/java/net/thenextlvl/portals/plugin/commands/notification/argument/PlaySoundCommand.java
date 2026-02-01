package net.thenextlvl.portals.plugin.commands.notification.argument;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.RegistryArgumentExtractor;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.sound.Sound;
import net.thenextlvl.portals.notification.NotificationType;
import net.thenextlvl.portals.plugin.PortalsPlugin;
import net.thenextlvl.portals.plugin.commands.arguments.EnumArgumentType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PlaySoundCommand extends PortalNotificationCommand<Sound> {
    private PlaySoundCommand(final PortalsPlugin plugin) {
        super(plugin, NotificationType.sound());
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PortalsPlugin plugin) {
        final var command = new PlaySoundCommand(plugin);
        final var sound = soundArgument().executes(command);
        final var soundSource = soundSourceArgument().executes(command);
        final var volume = Commands.argument("volume", FloatArgumentType.floatArg(0)).executes(command);
        final var pitch = Commands.argument("pitch", FloatArgumentType.floatArg(0, 2)).executes(command);
        return command.create().then(sound.then(soundSource.then(volume.then(pitch))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> soundArgument() {
        return Commands.argument("sound", ArgumentTypes.resourceKey(RegistryKey.SOUND_EVENT));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> soundSourceArgument() {
        return Commands.argument("sound-source", new EnumArgumentType<>(Sound.Source.class));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var source = tryGetArgument(context, "source", Sound.Source.class).orElse(Sound.Source.MASTER);
        final var volume = tryGetArgument(context, "volume", float.class).orElse(1f);
        final var pitch = tryGetArgument(context, "pitch", float.class).orElse(1f);
        final var sound = RegistryArgumentExtractor.getTypedKey(context, RegistryKey.SOUND_EVENT, "sound");
        return addAction(context, Sound.sound(sound, source, volume, pitch));
    }
}
