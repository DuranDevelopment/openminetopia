package nl.openminetopia.api.player.objects;

import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
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

    /* Fitness */
    void setTotalPoints(int points);
    int getTotalPoints();
    void setClimbingPoints(int points);
    int getClimbingPoints();
    void setWalkingPoints(int points);
    int getWalkingPoints();
}
