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
@Table(name = "fitness_boosters")
public class FitnessBoosterModel extends StormModel {

    @Column(
            keyType = KeyType.FOREIGN,
            references = {FitnessModel.class}
    )
    private Integer fitnessId;

    @Column(name = "fitness", defaultValue = "0")
    private Integer fitness;

    @Column(name = "expires_at")
    private Long expiresAt;
}
