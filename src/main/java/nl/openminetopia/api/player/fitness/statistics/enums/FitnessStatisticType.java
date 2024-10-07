package nl.openminetopia.api.player.fitness.statistics.enums;

import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.types.*;

public enum FitnessStatisticType {
    TOTAL,
    WALKING,
    SPRINTING,
    CLIMBING,
    SWIMMING,
    FLYING,
    DRINKING,
    EATING,
    HEALTH;

    public Class<? extends FitnessStatistic> correspondingClass() {
        return switch (this) {
            case TOTAL -> TotalStatistic.class;
            case WALKING -> WalkingStatistic.class;
            case SPRINTING -> SprintingStatistic.class;
            case CLIMBING -> ClimbingStatistic.class;
            case SWIMMING -> SwimmingStatistic.class;
            case FLYING -> FlyingStatistic.class;
            case DRINKING -> DrinkingStatistic.class;
            case EATING -> EatingStatistic.class;
            case HEALTH -> HealthStatistic.class;
        };
    }

}
