package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;

public class LevelManager {

    private static LevelManager instance;

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public void setLevel(MinetopiaPlayer player, int level) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setLevel(level));
    }
}
