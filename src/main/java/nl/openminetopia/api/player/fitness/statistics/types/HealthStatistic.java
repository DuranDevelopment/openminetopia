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
public class HealthStatistic extends FitnessStatistic {

    private int points;

    public HealthStatistic(int fitnessGained, int points) {
        super(FitnessStatisticType.HEALTH, OpenMinetopia.getDefaultConfiguration().getMaxFitnessByHealth(), fitnessGained);
        this.points = points;
    }
}
