package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "cities")
public class CityModel extends StormModel {

    @Column(name = "city_name", unique = true)
    private String cityName;

    @Column(name = "world_id")
    private Integer worldId;

    @Column(name = "color")
    private String color;

    @Column(name = "title")
    private String title;

    @Column(name = "loading_name")
    private String loadingName;

    @Column(name = "temperature")
    private Double temperature;
}
