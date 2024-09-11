package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "prefix_colors")
public class PrefixColorsModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;

    @Column(name = "color", defaultValue = "<gray>")
    private String color;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

}
