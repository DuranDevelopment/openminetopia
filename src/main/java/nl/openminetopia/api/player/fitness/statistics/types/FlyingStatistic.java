package nl.openminetopia.api.player.fitness.statistics.types;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlyingStatistic extends FitnessStatistic {

    public FlyingStatistic(int fitnessGained) {
        super(FitnessStatisticType.FLYING, OpenMinetopia.getDefaultConfiguration().getMaxFitnessByFlying(), fitnessGained);
    }
}
