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
public class TotalStatistic extends FitnessStatistic {

    public TotalStatistic(int fitnessGained) {
        super(FitnessStatisticType.TOTAL, OpenMinetopia.getDefaultConfiguration().getMaxFitnessLevel(), fitnessGained);
    }
}
