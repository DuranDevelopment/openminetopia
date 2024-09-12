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
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PlayerModel playerModel = StormDatabase.getInstance().getStorm().buildQuery(PlayerModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (playerModel != null) {
                    playerModel.setLevel(level);
                    StormDatabase.getInstance().saveStormModel(playerModel);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
