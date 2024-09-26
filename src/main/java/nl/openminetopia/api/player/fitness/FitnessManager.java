package nl.openminetopia.api.player.fitness;

import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FitnessManager {

    private static FitnessManager instance;

    public static FitnessManager getInstance() {
        if (instance == null) {
            instance = new FitnessManager();
        }
        return instance;
    }

    private final Map<UUID, Fitness> fitnesses = new HashMap<>();

    public Fitness getFitness(MinetopiaPlayer minetopiaPlayer) {
        Fitness fitness = fitnesses.get(minetopiaPlayer.getUuid());
        if (!fitnesses.containsKey(minetopiaPlayer.getUuid())) {
            fitness = new Fitness(minetopiaPlayer);
            fitness.load();
            fitnesses.put(minetopiaPlayer.getUuid(), fitness);
        }
        return fitness;
    }

    public CompletableFuture<Integer> getTotalFitness(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getPlayerRelatedModelData(player, FitnessModel.class, query -> {
        }, model -> true, FitnessModel::getTotal, 0);
    }

    public void setTotalFitness(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setTotal(amount));
    }

    public CompletableFuture<Integer> getFitnessGainedByDrinking(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getPlayerRelatedModelData(player, FitnessModel.class, query -> {
        }, model -> true, FitnessModel::getFitnessGainedByDrinking, 0);
    }

    public CompletableFuture<Integer> getFitnessGainedByHealth(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getPlayerRelatedModelData(player, FitnessModel.class, query -> {
        }, model -> true, FitnessModel::getFitnessGainedByHealth, 0);
    }

    public CompletableFuture<Double> getDrinkingPoints(MinetopiaPlayer player) {
        return StormDatabase.getInstance().getPlayerRelatedModelData(player, FitnessModel.class, query -> {
        }, model -> true, FitnessModel::getDrinkingPoints, 0.0);
    }

    public void setDrinkingPoints(MinetopiaPlayer player, double amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setDrinkingPoints(amount));
    }

    public void setHealthPoints(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setHealthPoints(amount));
    }

    /**
     * Set fitness gained
     */
    public void setFitnessGainedByHealth(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByHealth(amount));
    }

    public void setFitnessGainedByDrinking(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByDrinking(amount));
    }

    public void setFitnessGainedByClimbing(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByClimbing(amount));
    }

    public void setFitnessGainedBySprinting(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedBySprinting(amount));
    }

    public void setFitnessGainedByFlying(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByFlying(amount));
    }

    public void setFitnessGainedBySwimming(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedBySwimming(amount));
    }

    public void setFitnessGainedByWalking(MinetopiaPlayer player, int amount) {
        StormDatabase.getInstance().updatePlayerRelatedModel(player, FitnessModel.class, fitnessModel -> fitnessModel.setFitnessGainedByWalking(amount));
    }
}
