package nl.openminetopia.api.player.fitness.statistics.types;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.configuration.FitnessConfiguration;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class EatingStatistic extends FitnessStatistic {

    private int luxuryFood;
    private int cheapFood;
    private double points;

    private final FitnessConfiguration fitnessConfiguration = OpenMinetopia.getFitnessConfiguration();

    public EatingStatistic(int fitnessGained, int luxuryFood, int cheapFood) {
        super(FitnessStatisticType.EATING, OpenMinetopia.getFitnessConfiguration().getMaxFitnessByHealth(), fitnessGained);
        this.luxuryFood = luxuryFood;
        this.cheapFood = cheapFood;
        this.points = (luxuryFood * fitnessConfiguration.getPointsForLuxuryFood()) + (cheapFood * fitnessConfiguration.getPointsForCheapFood());
    }
}
