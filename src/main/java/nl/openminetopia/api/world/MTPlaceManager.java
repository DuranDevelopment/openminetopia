package nl.openminetopia.api.world;

import nl.openminetopia.api.world.objects.MTCity;
import nl.openminetopia.api.world.objects.MTPlace;
import org.bukkit.entity.Player;

public class MTPlaceManager {

    private static MTPlaceManager instance;

    public static MTPlaceManager getInstance() {
        if (instance == null) {
            instance = new MTPlaceManager();
        }
        return instance;
    }

    public MTPlace getPlace(Player player) {
        MTCity city = MTCityManager.getInstance().getCity(player);
        if (city == null) return MTWorldManager.getInstance().getWorld(player);
        return city;
    }
}