package nl.openminetopia.api.player;

import nl.openminetopia.modules.data.storm.models.PlayerModel;

import java.util.UUID;

public interface MinetopiaPlayer {

    UUID getUuid();
    PlayerModel getPlayerModel();

    int getLevel();

}
