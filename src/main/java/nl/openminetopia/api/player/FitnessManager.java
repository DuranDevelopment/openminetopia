package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

public class FitnessManager {

    private static FitnessManager instance;

    public static FitnessManager getInstance() {
        if (instance == null) {
            instance = new FitnessManager();
        }
        return instance;
    }

    public void setTotalPoints(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);


                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setTotal(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setClimbingPoints(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setClimbingPoints(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }


    public void setWalkingPoints(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setWalkingPoints(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
