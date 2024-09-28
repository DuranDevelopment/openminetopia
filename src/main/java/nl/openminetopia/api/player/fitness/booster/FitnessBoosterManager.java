package nl.openminetopia.api.player.fitness.booster;

import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessBoosterModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class FitnessBoosterManager {

    private static FitnessBoosterManager instance;

    public static FitnessBoosterManager getInstance() {
        if (instance == null) {
            instance = new FitnessBoosterManager();
        }
        return instance;
    }

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    public void addFitnessBooster(MinetopiaPlayer player, FitnessBooster booster) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessBoosterModel fitnessBoosterModel = new FitnessBoosterModel();
                fitnessBoosterModel.setUniqueId(player.getUuid());
                fitnessBoosterModel.setFitness(booster.getAmount());
                fitnessBoosterModel.setExpiresAt(booster.getExpiresAt());

                StormDatabase.getInstance().saveStormModel(fitnessBoosterModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void removeFitnessBooster(Fitness fitness, FitnessBooster booster) {
        dataModule.getAdapter().removeFitnessBooster(fitness, booster);
    }

    public CompletableFuture<List<FitnessBooster>> getFitnessBoosters(Fitness fitness) {
        return dataModule.getAdapter().getFitnessBoosters(fitness);
    }
}
