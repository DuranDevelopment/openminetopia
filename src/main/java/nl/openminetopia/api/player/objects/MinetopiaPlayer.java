package nl.openminetopia.api.player.objects;

import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public interface MinetopiaPlayer {

    UUID getUuid();
    PlayerModel getPlayerModel();

    OfflinePlayer getBukkit();

    /* Playtime */
    void setPlaytime(int playtime, boolean updateDatabase);
    /** Returns the playtime in seconds */
    int getPlaytime();

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
    Fitness getFitness();
    void setFitness(Fitness fitness);

    /* Places */
    boolean isInPlace();
    MTPlace getPlace();
    MTWorld getWorld();
}
