package nl.openminetopia.api.player.objects;

import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.modules.color.objects.PrefixColor;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public interface MinetopiaPlayer {

    UUID getUuid();
    PlayerModel getPlayerModel();

    OfflinePlayer getBukkit();

    /* Level */
    void setLevel(int level);
    int getLevel();

    /* Prefixes */
    List<Prefix> getPrefixes();
    void addPrefix(Prefix prefix);
    void removePrefix(Prefix prefix);

    Prefix getActivePrefix();
    void setActivePrefix(Prefix prefix);

    /* Prefix Colors */
    List<PrefixColor> getPrefixColors();
    void addPrefixColor(PrefixColor color);
    void removePrefixColor(PrefixColor color);

    PrefixColor getActivePrefixColor();
    void setActivePrefixColor(PrefixColor color);


    /* Fitness */
    void setLastDrinkingTime(long time);
    long getLastDrinkingTime();

    void setFitness(int amount);
    int getFitness();

    int getFitnessGainedByDrinking();
    void setFitnessGainedByDrinking(int points);

    void setDrinkingPoints(double points);
    double getDrinkingPoints();

    void setFitnessGainedByClimbing(int points);
    int getFitnessGainedByClimbing();

    void setFitnessGainedByWalking(int points);
    int getFitnessGainedByWalking();

    void setFitnessGainedBySprinting(int points);
    int getFitnessGainedBySprinting();

    void setFitnessGainedBySwimming(int points);
    int getFitnessGainedBySwimming();

    void setFitnessGainedByFlying(int points);
    int getFitnessGainedByFlying();
}
