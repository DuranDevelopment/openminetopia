package nl.openminetopia.api.player.fitness.statistics.types;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.configuration.DefaultConfiguration;

@EqualsAndHashCode(callSuper = true)
@Data
public class EatingStatistic extends FitnessStatistic {

    private int luxuryFood;
    private int cheapFood;
    private double points;

    private final DefaultConfiguration defaultConfiguration = OpenMinetopia.getDefaultConfiguration();

    public EatingStatistic(int fitnessGained, int luxuryFood, int cheapFood) {
        super(FitnessStatisticType.EATING, OpenMinetopia.getDefaultConfiguration().getMaxFitnessByHealth(), fitnessGained);
        this.luxuryFood = luxuryFood;
        this.cheapFood = cheapFood;
        this.points = (luxuryFood * defaultConfiguration.getPointsForLuxuryFood()) + (cheapFood * defaultConfiguration.getPointsForCheapFood());
    }
}
