package nl.openminetopia.configuration;

import lombok.Getter;
import nl.openminetopia.configuration.components.ColorComponent;
import nl.openminetopia.utils.ConfigurateConfig;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ColorsConfiguration extends ConfigurateConfig {

    private final Map<String, ColorComponent> components = new HashMap<>();

    public ColorsConfiguration(File file) {
        super(file, "colors.yml");

        ConfigurationNode colorsNode = rootNode.node("colors");
        colorsNode.childrenMap().forEach((o, colorNode) -> {
            String identifier = (String) o;
            String displayName = colorNode.node("display_name").getString();
            String colorPrefix = colorNode.node("color_prefix").getString();

            components.put(identifier, new ColorComponent(identifier, displayName, colorPrefix));
        });
    }

    public ColorComponent color(String identifier) {
        return components.get(identifier);
    }

    public boolean exists(String identifier) {
        return components.containsKey(identifier);
    }

}
