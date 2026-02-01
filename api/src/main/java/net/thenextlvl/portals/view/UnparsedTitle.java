package net.thenextlvl.portals.view;

import net.kyori.adventure.title.Title;
import org.jspecify.annotations.Nullable;

/**
 * A title that has not been parsed yet.
 *
 * @param title    the title
 * @param subtitle the subtitle
 * @param times    the times
 * @since 1.4.0
 */
public record UnparsedTitle(String title, String subtitle, Title.@Nullable Times times) {
}
