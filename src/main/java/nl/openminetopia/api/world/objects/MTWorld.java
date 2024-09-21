package nl.openminetopia.api.world.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MTWorld implements MTPlace {

    private int id;
    private String name;
    private String title;
    private String color;
    private double temperature;
    private String loadingName;

    public MTWorld(String name, String title, String color, double temperature, String loadingName) {
        this.name = name;
        this.title = title;
        this.color = color;
        this.temperature = temperature;
        this.loadingName = loadingName;
    }
}
