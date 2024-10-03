package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "colors")
public class ColorModel extends StormModel {

    @Column(
            keyType = KeyType.FOREIGN,
            references = {PlayerModel.class}
    )
    private Integer playerId;

    @Column(name = "color_id")
    private String colorId;

    @Column(name = "type")
    private String type;

    @Column(name = "expires_at")
    private Long expiresAt;

}
