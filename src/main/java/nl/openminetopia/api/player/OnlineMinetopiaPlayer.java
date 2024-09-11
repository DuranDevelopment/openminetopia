package nl.openminetopia.api.player;

import lombok.Getter;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;

import java.util.UUID;

@Getter
public class OnlineMinetopiaPlayer implements MinetopiaPlayer {

    private final UUID uuid;
    private final PlayerModel playerModel;
    private int level;

    public OnlineMinetopiaPlayer(UUID uuid, PlayerModel playerModel) {
        this.uuid = uuid;
        this.playerModel = playerModel;
    }

    public void load() {
        System.out.println(playerModel.getLevel());
        this.level = playerModel.getLevel();
    }

    public void setLevel(int level) {
        this.level = level;

        // Update the player model
        playerModel.setLevel(level);
        StormDatabase.getInstance().saveStormModel(playerModel);
    }
}