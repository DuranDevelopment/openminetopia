package nl.openminetopia.modules.data.storm.models;


import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.ColumnType;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "fitness")
public class FitnessModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;

    @Column(name = "total", defaultValue = "20")
    private Integer total = 0;

    @Column(name = "fitness_by_drinking", defaultValue = "0")
    private Integer fitnessGainedByDrinking = 0;

    @Column(name = "fitness_by_walking", defaultValue = "0")
    private Integer fitnessGainedByWalking = 0;

    @Column(name = "fitness_by_climbing", defaultValue = "0")
    private Integer fitnessGainedByClimbing = 0;

    @Column(name = "fitness_by_sprinting", defaultValue = "0")
    private Integer fitnessGainedBySprinting = 0;

    @Column(name = "fitness_by_swimming", defaultValue = "0")
    private Integer fitnessGainedBySwimming = 0;

    @Column(name = "fitness_by_flying", defaultValue = "0")
    private Integer fitnessGainedByFlying = 0;

    @Column(name = "fitness_by_health", defaultValue = "0")
    private Integer fitnessGainedByHealth = 0;

    @Column(name = "fitness_by_eating", defaultValue = "0")
    private Integer fitnessGainedByEating = 0;

    @Column(name = "drinking_points", defaultValue = "0")
    private Double drinkingPoints = 0.0;

    @Column(name = "health_points", defaultValue = "0")
    private Integer healthPoints = 0;

    @Column(name = "luxury_food", defaultValue = "0")
    private Integer luxuryFood = 0;

    @Column(name = "cheap_food", defaultValue = "0")
    private Integer cheapFood = 0;

    @Column(
            type = ColumnType.ONE_TO_MANY,
            references = FitnessBoosterModel.class,
            matchTo = "fitness_id"
    )
    private List<FitnessBoosterModel> boosters = new ArrayList<>();
}
