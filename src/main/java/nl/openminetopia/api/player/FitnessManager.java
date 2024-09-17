package nl.openminetopia.api.player;

import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

import java.util.concurrent.CompletableFuture;

public class FitnessManager {

    private static FitnessManager instance;

    public static FitnessManager getInstance() {
        if (instance == null) {
            instance = new FitnessManager();
        }
        return instance;
    }

    public void setFitness(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setTotal(amount));
    }

    public CompletableFuture<Integer> getFitnessGainedByDrinking(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getModelData(player, FitnessModel.class, query -> {}, model -> true, FitnessModel::getFitnessGainedByDrinking, 0);
    }

    public CompletableFuture<Double> getDrinkingPoints(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getModelData(player, FitnessModel.class, query -> {}, model -> true, FitnessModel::getDrinkingPoints, 0.0);
    }

    public void setDrinkingPoints(MinetopiaPlayer player, double amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setDrinkingPoints(amount));
    }

    /**
     * Set fitness gained
     */

    public void setFitnessGainedByDrinking(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByDrinking(amount));
    }

    public void setFitnessGainedByClimbing(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByClimbing(amount));
    }

    public void setFitnessGainedBySprinting(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedBySprinting(amount));
    }

    public void setFitnessGainedByFlying(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByFlying(amount));
    }

    public void setFitnessGainedBySwimming(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedBySwimming(amount));
    }

    public void setFitnessGainedByWalking(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updateModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByWalking(amount));
    }
}
