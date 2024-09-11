package nl.openminetopia.api.player.objects;

import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.List;
import java.util.UUID;

public interface MinetopiaPlayer {

    UUID getUuid();
    PlayerModel getPlayerModel();

    void setLevel(int level);
    int getLevel();
    List<Prefix> getPrefixes();
    void addPrefix(Prefix prefix);
    Prefix getActivePrefix();
    void setActivePrefix(Prefix prefix);
}
