package nl.openminetopia.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import org.apache.commons.io.FileUtils;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Getter
public abstract class ConfigurateConfig {
    protected final YamlConfigurationLoader loader;
    protected ConfigurationNode rootNode;

    public ConfigurateConfig(File file, String name, String def) {
        if (def != null && !def.isEmpty()) this.defaultConfig(file, name, def);
        loader = YamlConfigurationLoader.builder()
                .path(file.toPath().resolve(name))
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .headerMode(HeaderMode.PRESET)
                .build();

        try {
            rootNode = loader.load();
        } catch (IOException e) {
            OpenMinetopia.getInstance().getLogger().warning("An error occurred while loading this configuration: " + e.getMessage());
        }
    }

    public void saveConfiguration() {
        try {
            loader.save(rootNode);
        } catch (Exception e) {
            OpenMinetopia.getInstance().getLogger().warning("Unable to save your messages configuration! Sorry! " + e.getMessage());
        }
    }

    @SneakyThrows
    private void defaultConfig(File file, String name, String def) {
        File config = new File(file, name);
        if (!config.exists()) {
            InputStream resourceStream = OpenMinetopia.class.getResourceAsStream("/" + def);
            if (resourceStream == null) {
                OpenMinetopia.getInstance().getLogger().warning("Could not find def resource: " + def);
                return;
            }

            FileUtils.copyInputStreamToFile(resourceStream, config);
        }
    }

}