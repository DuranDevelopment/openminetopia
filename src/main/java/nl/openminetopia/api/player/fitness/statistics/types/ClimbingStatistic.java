package nl.openminetopia.api.player.fitness.statistics.types;

import lombok.*;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClimbingStatistic extends FitnessStatistic {

    public ClimbingStatistic(int fitnessGained) {
        super(FitnessStatisticType.CLIMBING, OpenMinetopia.getDefaultConfiguration().getMaxFitnessByClimbing(), fitnessGained);
    }
}
