package nl.openminetopia.configuration;

import lombok.Getter;
import nl.openminetopia.modules.data.type.DatabaseType;
import nl.openminetopia.utils.ConfigurateConfig;

import java.io.File;

@Getter
public class DefaultConfiguration extends ConfigurateConfig {

    /*
     * Database configuration
     */
    private final DatabaseType databaseType;
    private final String host;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;

    /*
     * Chat configuration
     */
    private final String chatFormat;

    /**
     * Fitness configuration
     */
    private final int walkingPointsPerCm;
    private final int climbingPointsPerCm;

    public DefaultConfiguration(File file) {
        super(file, "config.yml");

        /*
         * Database configuration
         */
        this.databaseType = DatabaseType.valueOf(rootNode.node("database", "type").getString("mysql").toUpperCase());
        this.host = rootNode.node("database", "host").getString("localhost");
        this.port = rootNode.node("database", "port").getInt(3306);
        this.databaseName = rootNode.node("database", "name").getString("openminetopia");
        this.username = rootNode.node("database", "username").getString("root");
        this.password = rootNode.node("database", "password").getString("password");

        /*
         * Fitness configuration
         */
        this.walkingPointsPerCm = rootNode.node("fitness", "walkingPointsPerCm").getInt(1000000);
        this.climbingPointsPerCm = rootNode.node("fitness", "climbingPointsPerCm").getInt(1000000);

        /*
         * Chat configuration
         */
        this.chatFormat = rootNode.node("chat", "format").getString("<dark_gray>[<levelcolor>Level <level><dark_gray>] <dark_gray>[<prefixcolor><prefix><dark_gray>] <namecolor><name>: <chatcolor><message>");
    }
}