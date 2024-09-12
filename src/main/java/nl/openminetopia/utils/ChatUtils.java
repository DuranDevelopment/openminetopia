package nl.openminetopia.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class ChatUtils {

    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static String stripMiniMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
