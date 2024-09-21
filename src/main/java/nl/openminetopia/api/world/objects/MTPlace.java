package nl.openminetopia.api.world.objects;

public interface MTPlace {
    void setName(String cityName);

    String getTitle();

    void setColor(String color);

    String getName();

    String getColor();

    double getTemperature();

    String getLoadingName();

    void setTemperature(double temperature);

    void setLoadingName(String loadingName);
}
