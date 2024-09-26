package nl.openminetopia.api.places;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.CityModel;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class MTCityManager {

    private static MTCityManager instance;

    public static MTCityManager getInstance() {
        if (instance == null) {
            instance = new MTCityManager();
        }
        return instance;
    }

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    private final List<MTCity> cities = new ArrayList<>();

    public MTCity getCity(String name) {
        for (MTCity city : cities) {
            if (!city.getName().equalsIgnoreCase(name)) continue;
            return city;
        }
        return null;
    }

    public void createCity(MTCity city) {
        dataModule.getAdapter().createCity(city);
        cities.add(city);
    }

    public void deleteCity(MTCity city) {
        dataModule.getAdapter().deleteCity(city);
        cities.remove(city);
    }

    public MTCity getCity(Location location) {
        ProtectedRegion region = WorldGuardUtils.getProtectedRegion(location, priority -> priority >= 0);

        if (region == null) return null;

        for (MTCity city : cities) {
            if (!city.getName().equalsIgnoreCase(region.getId())) continue;
            return city;
        }
        return null;
    }

    public CompletableFuture<List<MTCity>> loadCities() {
        CompletableFuture<List<MTCity>> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                StormDatabase.getInstance().getStorm().buildQuery(CityModel.class)
                        .execute()
                        .whenComplete((cityModels, throwable) -> {
                                    if (throwable != null) {
                                        throwable.printStackTrace();
                                        return;
                                    }

                                    List<MTCity> cities = new ArrayList<>();
                                    for (CityModel model : cityModels) {
                                        MTCity city = new MTCity(model.getWorldId(), model.getCityName(), model.getTitle(), model.getColor(), model.getTemperature(), model.getLoadingName());
                                        cities.add(city);
                                    }

                                    cities.forEach(city -> {
                                        if (this.cities.contains(city)) return;
                                        this.cities.add(city);
                                    });
                                    completableFuture.complete(cities);
                                }
                        );
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public void setColor(MTCity city, String color) {
        cities.forEach(mtCity -> {
            if (!mtCity.getName().equals(city.getName())) return;
            mtCity.setColor(color);
        });
        dataModule.getAdapter().setColor(city, color);
    }

    public void setTemperature(MTCity city, Double temperature) {
        cities.forEach(mtCity -> {
            if (!mtCity.getName().equals(city.getName())) return;
            mtCity.setTemperature(temperature);
        });
        dataModule.getAdapter().setTemperature(city, temperature);
    }

    public void setLoadingName(MTCity city, String loadingName) {
        cities.forEach(mtCity -> {
            if (!mtCity.getName().equals(city.getName())) return;
            mtCity.setLoadingName(loadingName);
        });
        dataModule.getAdapter().setLoadingName(city, loadingName);
    }

    public void setTitle(MTCity city, String title) {
        cities.forEach(mtCity -> {
            if (!mtCity.getName().equals(city.getName())) return;
            mtCity.setTitle(title);
        });
        dataModule.getAdapter().setTitle(city, title);
    }

}