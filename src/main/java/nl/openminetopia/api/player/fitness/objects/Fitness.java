package nl.openminetopia.api.player.fitness.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class Fitness {

    private final UUID uuid;

    private List<FitnessStatistic> statistics;
    private @Setter long lastDrinkingTime;
    private List<FitnessBooster> boosters;

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    public Fitness(UUID uuid) {
        this.uuid = uuid;
    }

    public CompletableFuture<Void> load() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        dataModule.getAdapter().getStatistics(this).whenComplete((statistics, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.statistics = statistics;
        });

        if (this.statistics == null) {
            this.statistics = List.of(
                    new WalkingStatistic(0),
                    new ClimbingStatistic(0),
                    new DrinkingStatistic(0, 0),
                    new EatingStatistic(0, 0, 0),
                    new FlyingStatistic(0),
                    new WalkingStatistic(0),
                    new SprintingStatistic(0),
                    new SwimmingStatistic(0),
                    new HealthStatistic(0, 0),
                    new TotalStatistic(0)
            );

            StormDatabase.getExecutorService().execute(() -> {
                FitnessModel model = new FitnessModel();
                model.setUniqueId(uuid);
                StormDatabase.getInstance().saveStormModel(model).whenComplete((unused, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    dataModule.getAdapter().saveStatistics(this);
                });
            });
        }

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

    public void save() {
        dataModule.getAdapter().saveStatistics(this);
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
        boosters.remove(booster);
        dataModule.getAdapter().removeFitnessBooster(this, booster);
    }
}
