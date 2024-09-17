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
    private final int maxFitnessLevel;
    private final int defaultFitnessLevel;

    private final int maxFitnessByDrinking;
    private final double drinkingPointsPerPotion;
    private final double drinkingPointsPerWaterBottle;
    private final int drinkingPointsPerFitnessPoint;
    private final int drinkingCooldown;

    private final int maxFitnessByWalking;
    private final int cmPerWalkingPoint;

    private final int maxFitnessByClimbing;
    private final int cmPerClimbingPoint;

    private final int maxFitnessBySprinting;
    private final int cmPerSprintingPoint;

    private final int maxFitnessBySwimming;
    private final int cmPerSwimmingPoint;

    private final int maxFitnessByFlying;
    private final int cmPerFlyingPoint;

    private final int maxFitnessByHealth;
    private final int pointsAbove9Hearts;
    private final int pointsBelow5Hearts;
    private final int pointsBelow2Hearts;

    private final boolean fitnessDeathPunishmentEnabled;
    private final int fitnessDeathPunishmentAmount;
    private final int fitnessDeathPunishmentDuration;

    /**
     * Scoreboard configuration
     */
    private final boolean scoreboardEnabled;
    private final List<String> scoreboardLines;

    /**
     * Default settings configuration
     */
    private final String defaultPrefix;
    private final String defaultPrefixColor;
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
        this.defaultPrefixColor = rootNode.node("default", "prefixColor").getString("<gray>");
        this.defaultLevel = rootNode.node("default", "level").getInt(1);

        /*
         * Fitness configuration
         */
        this.maxFitnessLevel = rootNode.node("fitness", "maxFitnessLevel").getInt(255);
        this.defaultFitnessLevel = rootNode.node("fitness", "defaultFitnessLevel").getInt(20);

        this.maxFitnessByDrinking = rootNode.node("fitness", "drinking", "maxFitnessByDrinking").getInt(20);
        this.drinkingPointsPerPotion = rootNode.node("fitness", "drinking", "drinkingPointsPerPotion").getDouble(0.05);
        this.drinkingPointsPerWaterBottle = rootNode.node("fitness", "drinking", "drinkingPointsPerWaterBottle").getDouble(0.02);
        this.drinkingPointsPerFitnessPoint = rootNode.node("fitness", "drinking", "drinkingPointsPerFitnessPoint").getInt(1);
        this.drinkingCooldown = rootNode.node("fitness", "drinking", "drinkingCooldown").getInt(5);

        this.maxFitnessByWalking = rootNode.node("fitness", "statistics", "maxFitnessByWalking").getInt(30);
        this.cmPerWalkingPoint = rootNode.node("fitness", "statistics", "cmPerWalkingPoint").getInt(1000000);

        this.maxFitnessBySprinting = rootNode.node("fitness", "statistics", "maxFitnessBySprinting").getInt(40);
        this.cmPerSprintingPoint = rootNode.node("fitness", "statistics", "cmPerSprintingPoint").getInt(2000000);

        this.maxFitnessByClimbing = rootNode.node("fitness", "statistics", "maxFitnessByClimbing").getInt(30);
        this.cmPerClimbingPoint = rootNode.node("fitness", "statistics", "cmPerClimbingPoint").getInt(500000);

        this.maxFitnessBySwimming = rootNode.node("fitness", "statistics", "maxFitnessBySwimming").getInt(30);
        this.cmPerSwimmingPoint = rootNode.node("fitness", "statistics", "cmPerSwimmingPoint").getInt(600000);

        this.maxFitnessByFlying = rootNode.node("fitness", "statistics", "maxFitnessBySwimming").getInt(30);
        this.cmPerFlyingPoint = rootNode.node("fitness", "statistics", "cmPerSwimmingPoint").getInt(3000000);

        this.maxFitnessByHealth = rootNode.node("fitness", "health", "maxFitnessByHealth").getInt(10);
        this.pointsAbove9Hearts = rootNode.node("fitness", "health", "pointsAbove9Hearts").getInt(60);
        this.pointsBelow5Hearts = rootNode.node("fitness", "health", "pointsBelow5Hearts").getInt(-50);
        this.pointsBelow2Hearts = rootNode.node("fitness", "health", "pointsBelow2Hearts").getInt(-75);

        this.fitnessDeathPunishmentDuration = rootNode.node("fitness", "deathPunishment", "duration").getInt(1440);
        this.fitnessDeathPunishmentEnabled = rootNode.node("fitness", "deathPunishment", "enabled").getBoolean(true);
        this.fitnessDeathPunishmentAmount = rootNode.node("fitness", "deathPunishment", "amount").getInt(-20);

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