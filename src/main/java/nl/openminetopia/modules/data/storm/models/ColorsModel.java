package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "colors")
public class ColorsModel extends StormModel {

    @Column(name = "uuid")
    private UUID uniqueId;

    @Column(name = "color", defaultValue = "<gray>")
    private String color;

    @Column(name = "type")
    private String type;

    @Column(name = "expires_at")
    private Long expiresAt;

}
