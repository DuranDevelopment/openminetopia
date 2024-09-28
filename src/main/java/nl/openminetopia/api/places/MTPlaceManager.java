package nl.openminetopia.api.places;

import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.api.places.objects.MTPlace;
import org.bukkit.Location;

public class MTPlaceManager {

    private static MTPlaceManager instance;

    public static MTPlaceManager getInstance() {
        if (instance == null) {
            instance = new MTPlaceManager();
        }
        return instance;
    }

    public MTPlace getPlace(Location location) {
        MTCity city = MTCityManager.getInstance().getCity(location);
        if (city == null) return MTWorldManager.getInstance().getWorld(location);
        return city;
    }
}