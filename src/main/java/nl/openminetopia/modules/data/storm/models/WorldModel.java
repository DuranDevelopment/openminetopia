package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "worlds")
public class WorldModel extends StormModel {

    @Column(name = "world_name", unique = true)
    private String worldName;

    @Column(name = "color")
    private String color;

    @Column(name = "title")
    private String title;

    @Column(name = "loading_name")
    private String loadingName;

    @Column(name = "temperature")
    private Double temperature;
}
