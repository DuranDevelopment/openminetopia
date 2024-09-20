package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;

import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<Integer> getLevel(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getModelData(player, PlayerModel.class, query -> {}, model -> true, PlayerModel::getLevel, 0);
    }
}
