package nl.openminetopia.api.player.fitness.statistics.types;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class DrinkingStatistic extends FitnessStatistic {

    private double points;

    public DrinkingStatistic(int fitnessGained, double points) {
        super(FitnessStatisticType.DRINKING, OpenMinetopia.getDefaultConfiguration().getMaxFitnessByDrinking(), fitnessGained);
        this.points = points;
    }
}