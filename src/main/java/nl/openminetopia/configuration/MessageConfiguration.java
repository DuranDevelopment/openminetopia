package nl.openminetopia.configuration;

import nl.openminetopia.modules.data.type.DatabaseType;
import nl.openminetopia.utils.ConfigurateConfig;

import java.io.File;

public class MessageConfiguration extends ConfigurateConfig {

    /*
     * Database configuration
     */
    private final DatabaseType databaseType;

    public MessageConfiguration(File file) {
        super(file, "messages.yml");

        this.databaseType = DatabaseType.valueOf(rootNode.node("database", "type").getString("mysql").toUpperCase());
    }
}