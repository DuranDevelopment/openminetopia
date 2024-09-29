package nl.openminetopia.api.player.fitness;

import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FitnessManager {

    private static FitnessManager instance;

    public static FitnessManager getInstance() {
        if (instance == null) {
            instance = new FitnessManager();
        }
        return instance;
    }

    private final Map<UUID, Fitness> fitnessMap = new HashMap<>();

    public Fitness getFitness(UUID uuid) {
        return fitnessMap.computeIfAbsent(uuid, Fitness::new);
    }

    public FitnessStatistic getStatistic(Fitness fitness, FitnessStatisticType type) {
        return fitness.getStatistics().stream().filter(statistic -> statistic.getType() == type).findFirst().orElse(null);
    }
}
