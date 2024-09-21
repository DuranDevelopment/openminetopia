package nl.openminetopia.api.world;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.openminetopia.api.world.objects.MTCity;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.CityModel;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MTCityManager {

    private static MTCityManager instance;

    public static MTCityManager getInstance() {
        if (instance == null) {
            instance = new MTCityManager();
        }
        return instance;
    }

    private final List<MTCity> cities = new ArrayList<>();

    public MTCity getCity(Player player) {
        ProtectedRegion region = WorldGuardUtils.getProtectedRegion(player.getLocation(), priority -> priority >= 0);

        for (MTCity city : cities) {
            if (city.getName().equalsIgnoreCase(region.getId())) continue;
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
                                MTCity city = new MTCity(model.getCityName(), model.getTitle(), model.getColor(), model.getTemperature(), model.getLoadingName());
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
}
