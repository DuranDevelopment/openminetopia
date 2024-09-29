package nl.openminetopia.api.player.fitness.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class FitnessStatistic {

    public final FitnessStatisticType type;
    public final int maxFitnessGainable;
    public int fitnessGained;

}