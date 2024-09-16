package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
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

    @Column(name = "drinking_points", defaultValue = "0")
    private Double drinkingPoints;

    @Column(name = "fitness_by_drinking", defaultValue = "0")
    private Integer fitnessGainedByDrinking;

    @Column(name = "fitness_by_walking", defaultValue = "0")
    private Integer fitnessGainedByWalking;

    @Column(name = "fitness_by_climbing", defaultValue = "0")
    private Integer fitnessGainedByClimbing;
}
