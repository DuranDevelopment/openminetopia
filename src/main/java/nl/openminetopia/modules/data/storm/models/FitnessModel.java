package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "fitness")
public class FitnessModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;

    @Column(name = "total", defaultValue = "20")
    private Integer total;

    @Column(name = "fitness_by_drinking", defaultValue = "0")
    private Integer fitnessGainedByDrinking;

    @Column(name = "fitness_by_walking", defaultValue = "0")
    private Integer fitnessGainedByWalking;

    @Column(name = "fitness_by_climbing", defaultValue = "0")
    private Integer fitnessGainedByClimbing;

    @Column(name = "fitness_by_sprinting", defaultValue = "0")
    private Integer fitnessGainedBySprinting;

    @Column(name = "fitness_by_swimming", defaultValue = "0")
    private Integer fitnessGainedBySwimming;

    @Column(name = "fitness_by_flying", defaultValue = "0")
    private Integer fitnessGainedByFlying;

    @Column(name = "fitness_by_health", defaultValue = "0")
    private Integer fitnessGainedByHealth;

    @Column(name = "fitness_by_eating", defaultValue = "0")
    private Integer fitnessGainedByEating;

    @Column(name = "drinking_points", defaultValue = "0")
    private Double drinkingPoints;

    @Column(name = "health_points", defaultValue = "0")
    private Integer healthPoints;

    @Column(name = "luxury_food", defaultValue = "0")
    private Integer luxuryFood;

    @Column(name = "cheap_food", defaultValue = "0")
    private Integer cheapFood;

}
