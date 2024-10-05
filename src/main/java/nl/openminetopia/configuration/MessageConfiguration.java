package nl.openminetopia.configuration;

import net.kyori.adventure.text.Component;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.data.types.DatabaseType;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.ConfigurateConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MessageConfiguration extends ConfigurateConfig {

    private final Map<String, String> messages = new HashMap<>();

    public MessageConfiguration(File file) {
        super(file, "messages.yml", "default-messages.yml");

        rootNode.childrenMap().forEach((s, node) -> {
            String identifier = (String) s;
            messages.put(identifier, node.getString());
        });
    }

    public static String message(String identifier) {
        if (OpenMinetopia.getMessageConfiguration() == null) return identifier + " was not found.";

        String message = OpenMinetopia.getMessageConfiguration().messages.get(identifier);
        if (message == null) message = identifier + " was not found.";

        return message;
    }

    public static Component component(String identifier) {
        return ChatUtils.color(message(identifier));
    }

}