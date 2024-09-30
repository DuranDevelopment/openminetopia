package nl.openminetopia.api.player.fitness.booster;

import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.modules.data.DataModule;

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

    public void addFitnessBooster(Fitness fitness, FitnessBooster booster) {
        dataModule.getAdapter().addFitnessBooster(fitness, booster);
    }

    public void removeFitnessBooster(Fitness fitness, FitnessBooster booster) {
        dataModule.getAdapter().removeFitnessBooster(fitness, booster);
    }

    public CompletableFuture<List<FitnessBooster>> getFitnessBoosters(Fitness fitness) {
        return dataModule.getAdapter().getFitnessBoosters(fitness);
    }
}
