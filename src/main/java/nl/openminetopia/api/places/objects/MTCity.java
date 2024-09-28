package nl.openminetopia.api.places.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MTCity implements MTPlace {

    private int worldId;
    private String name;
    private String title;
    private String color;
    private double temperature;
    private String loadingName;

    public MTCity(int worldId, String name, String title, String color, double temperature, String loadingName) {
        this.worldId = worldId;
        this.name = name;
        this.title = title;
        this.color = color;
        this.temperature = temperature;
        this.loadingName = loadingName;
    }
}
