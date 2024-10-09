package nl.openminetopia.configuration;

import lombok.Getter;
import nl.openminetopia.utils.ConfigurateConfig;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;

@Getter
public class LevelCheckConfiguration extends ConfigurateConfig {

    private final ConfigurationNode levelsNode = rootNode.node("levels");

    private final int maxLevel;

    private final int pointsNeededForLevelUp;

    private final int pointsPerPlot;
    private final int pointsPer5KBalance;
    private final int pointsPerVehicle;
    private final int pointsForPrefix;
    private final int pointsPerHourPlayed;
    private final int pointsPer20Fitness;

    private final boolean autoLevelUp;

    public LevelCheckConfiguration(File file) {
        super(file, "levelcheck.yml", "");

        ConfigurationNode levelCheckNode = rootNode.node("levelcheck");

        this.maxLevel = levelCheckNode.node("max-level").getInt(100);

        this.pointsNeededForLevelUp = levelCheckNode.node("points-needed-for-level-up").getInt(2500);

        this.pointsPerPlot = levelCheckNode.node("points-per-plot").getInt(4000);
        this.pointsPer5KBalance = levelCheckNode.node("points-per-account-balance").getInt(5000);
        this.pointsPerVehicle = levelCheckNode.node("points-per-vehicle").getInt(1200);
        this.pointsForPrefix = levelCheckNode.node("points-for-prefix").getInt(1750);
        this.pointsPerHourPlayed = levelCheckNode.node("points-per-hour-played").getInt(350);
        this.pointsPer20Fitness = levelCheckNode.node("points-per-fitness").getInt(1500);

        this.autoLevelUp = levelCheckNode.node("auto-level-up").getBoolean(false);

        for (int i = 1; i <= maxLevel; i++) {
            levelsNode.node(i, "cost").getInt(0);
            levelsNode.node(i, "wage").getInt(0);
        }
    }

    public int getLevelUpCost(int level) {
        return levelsNode.node(level, "cost").getInt(0);
    }

    public int getHourWage(int level) {
        return levelsNode.node(level, "wage").getInt(0);
    }

    public boolean isAutoLevelUp() {
        return autoLevelUp;
    }

}