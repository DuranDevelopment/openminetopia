package nl.openminetopia.api.player.objects;

import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;
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

    /* Colors */
    List<OwnableColor> getColors();
    void setActiveColor(OwnableColor color, OwnableColorType type);
    OwnableColor getActiveColor(OwnableColorType type);
    void addColor(OwnableColor color);
    void removeColor(OwnableColor color);

    /* Fitness */
    Fitness getFitness();
    void setFitness(Fitness fitness);

    /* Places */
    boolean isInPlace();
    MTPlace getPlace();
    MTWorld getWorld();
}
