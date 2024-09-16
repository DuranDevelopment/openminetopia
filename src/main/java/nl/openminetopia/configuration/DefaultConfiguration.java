package nl.openminetopia.configuration;

import lombok.Getter;
import nl.openminetopia.modules.data.type.DatabaseType;
import nl.openminetopia.utils.ConfigurateConfig;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.util.List;

@Getter
public class DefaultConfiguration extends ConfigurateConfig {

    /**
     * Database configuration
     */
    private final DatabaseType databaseType;
    private final String host;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;

    /**
     * Chat configuration
     */
    private final String chatFormat;
    private final boolean chatEnabled;
    private final boolean chatRadiusEnabled;
    private final int chatRadiusRange;
    private final boolean notifyWhenNobodyInRange;

    /**
     * Fitness configuration
     */
    private final int walkingPointsPerCm;
    private final int climbingPointsPerCm;

    /**
     * Scoreboard configuration
     */
    private final boolean scoreboardEnabled;
    private final List<String> scoreboardLines;

    /**
     * Default settings configuration
     */
    private final String defaultPrefix;
    private final int defaultLevel;


    public DefaultConfiguration(File file) throws SerializationException {
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
         * Default settings configuration
         */
        this.defaultPrefix = rootNode.node("default", "prefix").getString("Zwerver");
        this.defaultLevel = rootNode.node("default", "level").getInt(1);

        /*
         * Fitness configuration
         */
        this.walkingPointsPerCm = rootNode.node("fitness", "walkingPointsPerCm").getInt(1000000);
        this.climbingPointsPerCm = rootNode.node("fitness", "climbingPointsPerCm").getInt(1000000);

        /*
         * Chat configuration
         */
        this.chatFormat = rootNode.node("chat", "format").getString("<dark_gray>[<levelcolor>Level <level><dark_gray>] <dark_gray>[<prefixcolor><prefix><dark_gray>] <namecolor><name>: <chatcolor><message>");
        this.chatEnabled = rootNode.node("chat", "enabled").getBoolean(true);
        this.chatRadiusEnabled = rootNode.node("chat", "radius", "enabled").getBoolean(true);
        this.chatRadiusRange = rootNode.node("chat", "radius", "range").getInt(20);
        this.notifyWhenNobodyInRange = rootNode.node("chat", "radius", "notifyWhenNobodyInRange").getBoolean(false);

        /*
         * Scoreboard configuration
         */
        this.scoreboardEnabled = rootNode.node("scoreboard", "enabled").getBoolean(true);
        this.scoreboardLines = rootNode.node("scoreboard", "lines").getList(String.class, List.of(
                "<red>OpenMinetopia",
                "<gray>Fitheid: <fitness>",
                "<gray>Level: <level>",
                "<gray>Prefix: <prefix>"
        ));

    }
}