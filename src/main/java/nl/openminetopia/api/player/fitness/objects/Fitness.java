package nl.openminetopia.api.player.fitness.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class Fitness {

    private final UUID uuid;

    private @Setter FitnessModel fitnessModel;

    private List<FitnessStatistic> statistics;
    private @Setter long lastDrinkingTime;
    private List<FitnessBooster> boosters;

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    public Fitness(UUID uuid) {
        this.uuid = uuid;
        this.fitnessModel = new FitnessModel();
    }

    public CompletableFuture<Void> load() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        dataModule.getAdapter().getFitness(this).whenComplete((fitnessModel, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.fitnessModel = fitnessModel;
        });

        dataModule.getAdapter().getStatistics(this).whenComplete((statistics, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.statistics = statistics;
        });

        dataModule.getAdapter().getFitnessBoosters(this).whenComplete((boosters, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.boosters = boosters;
        });

        future.complete(null);
        return future;
    }

    public CompletableFuture<Void> save() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        dataModule.getAdapter().saveFitnessBoosters(this).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
        });
        dataModule.getAdapter().saveStatistics(this).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
        });

        future.complete(null);
        return future;
    }

    public FitnessStatistic getStatistic(FitnessStatisticType type) {
        if (statistics == null) return null;
        return statistics.stream().filter(statistic -> statistic.getType().equals(type)).findFirst().orElse(null);
    }

    public void addBooster(FitnessBooster booster) {
        boosters.add(booster);
        dataModule.getAdapter().addFitnessBooster(this, booster);
    }

    public void removeBooster(FitnessBooster booster) {
        System.out.println("removing booster " + booster.getAmount() + " - " + booster.getId());
        boosters.remove(booster);
        dataModule.getAdapter().removeFitnessBooster(this, booster);
    }
}
