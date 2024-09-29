package nl.openminetopia.modules.data.adapters;

import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.*;
import nl.openminetopia.modules.data.storm.models.CityModel;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.storm.models.WorldModel;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DatabaseAdapter {

    void connect();

    void disconnect();

    /* Player related database queries */

    CompletableFuture<PlayerModel> loadPlayer(UUID uuid);

    CompletableFuture<Void> savePlayer(MinetopiaPlayer player);

    CompletableFuture<Void> setPlaytime(MinetopiaPlayer player, int playtime);

    CompletableFuture<Integer> getPlaytime(MinetopiaPlayer player);

    CompletableFuture<Boolean> setStaffchatEnabled(MinetopiaPlayer player, boolean enabled);

    CompletableFuture<Boolean> setCommandSpyEnabled(MinetopiaPlayer player, boolean enabled);

    CompletableFuture<Boolean> setChatSpyEnabled(MinetopiaPlayer player, boolean enabled);

    CompletableFuture<Boolean> getCommandSpyEnabled(MinetopiaPlayer player);

    CompletableFuture<Boolean> getChatSpyEnabled(MinetopiaPlayer player);

    /* Prefix related database queries */

    CompletableFuture<Void> addPrefix(MinetopiaPlayer player, Prefix prefix);

    CompletableFuture<Void> removePrefix(MinetopiaPlayer player, Prefix prefix);

    CompletableFuture<Void> setActivePrefix(MinetopiaPlayer player, Prefix prefix);

    CompletableFuture<Prefix> getActivePrefix(MinetopiaPlayer player);

    CompletableFuture<List<Prefix>> getPrefixes(MinetopiaPlayer player);

    /* Color related database queries */

    CompletableFuture<Void> addColor(MinetopiaPlayer player, OwnableColor color);

    CompletableFuture<Void> removeColor(MinetopiaPlayer player, OwnableColor color);

    CompletableFuture<Void> setActiveColor(MinetopiaPlayer player, OwnableColor color, OwnableColorType type);

    CompletableFuture<OwnableColor> getActiveColor(MinetopiaPlayer player, OwnableColorType type);

    CompletableFuture<List<OwnableColor>> getColors(MinetopiaPlayer player);

    CompletableFuture<List<OwnableColor>> getColors(MinetopiaPlayer player, OwnableColorType type);

    /* Level related database queries */

    CompletableFuture<Integer> getLevel(MinetopiaPlayer player);

    CompletableFuture<Void> setLevel(MinetopiaPlayer player, int level);

    /* World related database queries */

    CompletableFuture<WorldModel> createWorld(MTWorld world);
    CompletableFuture<Void> deleteWorld(MTWorld world);

    CompletableFuture<Void> setTemperature(MTWorld world, double temperature);
    CompletableFuture<Void> setTitle(MTWorld world, String title);
    CompletableFuture<Void> setLoadingName(MTWorld world, String loadingName);
    CompletableFuture<Void> setColor(MTWorld world, String color);

    /* City related database queries */

    CompletableFuture<CityModel> createCity(MTCity city);
    CompletableFuture<Void> deleteCity(MTCity city);

    CompletableFuture<Void> setTemperature(MTCity city, double temperature);
    CompletableFuture<Void> setTitle(MTCity city, String title);
    CompletableFuture<Void> setLoadingName(MTCity city, String loadingName);
    CompletableFuture<Void> setColor(MTCity city, String color);
}
