package nl.openminetopia.modules.data.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.hikaricp.HikariDriver;
import com.zaxxer.hikari.HikariConfig;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.*;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.*;
import nl.openminetopia.modules.data.utils.StormUtils;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MySQLAdapter implements DatabaseAdapter {

    @Override
    public void connect() {
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();
        String host = configuration.getHost();
        int port = configuration.getPort();
        String name = configuration.getDatabaseName();
        String username = configuration.getUsername();
        String password = configuration.getPassword();

        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            StormDatabase.getInstance().setStorm(new Storm(new HikariDriver(config)));
        } catch (Exception e) {
            OpenMinetopia.getInstance().getLogger().severe("Failed to connect to MySQL database: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        StormDatabase.getInstance().getStorm().getDriver().close();
    }

    /* Player related database queries */

    @Override
    public CompletableFuture<PlayerModel> loadPlayer(UUID uuid) {
        CompletableFuture<PlayerModel> future = StormDatabase.getInstance().loadPlayerModel(uuid);
        future.whenComplete((playerModel, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
                OpenMinetopia.getInstance().getLogger().warning("Error loading player model: " + throwable.getMessage());
                return;
            }

            if (playerModel == null) {
                future.completeExceptionally(new NullPointerException("Player model is null"));
                return;
            }
            PlayerManager.getInstance().getPlayerModels().put(uuid, playerModel);
            future.complete(playerModel);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> savePlayer(MinetopiaPlayer player) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        StormUtils.updateModelData(PlayerModel.class, query -> query.where("uuid", Where.EQUAL, player.getUuid()), model -> {
            model.setLevel(player.getLevel());
            model.setActivePrefixId(player.getActivePrefix().getId());
            model.setActivePrefixColorId(player.getActiveColor(OwnableColorType.PREFIX).getId());
            model.setActiveChatColorId(player.getActiveColor(OwnableColorType.CHAT).getId());
            model.setActiveNameColorId(player.getActiveColor(OwnableColorType.NAME).getId());
            model.setActiveLevelColorId(player.getActiveColor(OwnableColorType.LEVEL).getId());
            model.setPlaytime(player.getPlaytime());

            if (player instanceof OnlineMinetopiaPlayer onlineMinetopiaPlayer) {
                model.setStaffchatEnabled(onlineMinetopiaPlayer.isStaffchatEnabled());
            }
        });
        future.complete(null);
        return future;
    }

    @Override
    public CompletableFuture<Integer> getPlaytime(MinetopiaPlayer player) {
        CompletableFuture<Integer> playtimeFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getPlaytime,
                0
        ).whenComplete((playtime, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                playtimeFuture.completeExceptionally(ex);
                return;
            }
            playtimeFuture.complete(playtime);
        });
        return playtimeFuture;  // Return the CompletableFuture
    }

    @Override
    public CompletableFuture<Void> setPlaytime(MinetopiaPlayer player, int playtime) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> playerModel.setPlaytime(playtime)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> setStaffchatEnabled(MinetopiaPlayer player, boolean enabled) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> playerModel.setStaffchatEnabled(enabled)
        );
        completableFuture.complete(enabled);
        return completableFuture;
    }

    /* Prefix related database queries */

    @Override
    public CompletableFuture<Void> addPrefix(MinetopiaPlayer player, Prefix prefix) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            PrefixModel prefixModel = new PrefixModel();
            prefixModel.setUniqueId(player.getUuid());
            prefixModel.setPrefix(prefix.getPrefix());
            prefixModel.setExpiresAt(prefix.getExpiresAt());

            StormDatabase.getInstance().saveStormModel(prefixModel);
            completableFuture.complete(null);
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> removePrefix(MinetopiaPlayer player, Prefix prefix) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.deleteModelData(PrefixModel.class,
                query -> query.where("id", Where.EQUAL, prefix.getId())
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setActivePrefix(MinetopiaPlayer player, Prefix prefix) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> playerModel.setActivePrefixId(prefix.getId())
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Prefix> getActivePrefix(MinetopiaPlayer player) {
        CompletableFuture<Prefix> prefixFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getActivePrefixId,
                null
        ).thenAccept(integer -> StormUtils.getModelData(PrefixModel.class,
                query -> query.where("id", Where.EQUAL, integer),
                null,
                prefixModel -> new Prefix(prefixModel.getId(), prefixModel.getPrefix(), prefixModel.getExpiresAt()),
                null
        ).whenComplete((prefix, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                prefixFuture.completeExceptionally(ex);
            } else {
                prefixFuture.complete(prefix);
            }
        }));

        return prefixFuture;
    }

    @Override
    public CompletableFuture<List<Prefix>> getPrefixes(MinetopiaPlayer player) {
        CompletableFuture<List<Prefix>> completableFuture = new CompletableFuture<>();

        findPrefixes(player).thenAccept(prefixesModels -> {
            // Create a list to store the prefixes
            List<Prefix> prefixes = new ArrayList<>();
            for (PrefixModel prefixModel : prefixesModels) {
                prefixes.add(new Prefix(prefixModel.getId(), prefixModel.getPrefix(), prefixModel.getExpiresAt()));
            }
            completableFuture.complete(prefixes);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setActiveColor(MinetopiaPlayer player, OwnableColor color, OwnableColorType type) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> {
                    switch (type) {
                        case PREFIX:
                            playerModel.setActivePrefixColorId(color.getId());
                            break;
                        case CHAT:
                            playerModel.setActiveChatColorId(color.getId());
                            break;
                        case NAME:
                            playerModel.setActiveNameColorId(color.getId());
                            break;
                        case LEVEL:
                            playerModel.setActiveLevelColorId(color.getId());
                            break;
                    }
                }
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    private CompletableFuture<List<PrefixModel>> findPrefixes(MinetopiaPlayer player) {
        CompletableFuture<List<PrefixModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<PrefixModel> prefixModel = StormDatabase.getInstance().getStorm().buildQuery(PrefixModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join();

                completableFuture.complete(new ArrayList<>(prefixModel));
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }

    /* Color related database queries */

    @Override
    public CompletableFuture<Void> addColor(MinetopiaPlayer player, OwnableColor color) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            ColorModel colorModel = new ColorModel();
            colorModel.setUniqueId(player.getUuid());
            colorModel.setColor(color.getColor());
            colorModel.setExpiresAt(color.getExpiresAt());
            colorModel.setType(color.getType().toString().toLowerCase());

            StormDatabase.getInstance().saveStormModel(colorModel);
        });
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> removeColor(MinetopiaPlayer player, OwnableColor color) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.deleteModelData(ColorModel.class,
                query -> query.where("id", Where.EQUAL, color.getId())
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<OwnableColor> getActiveColor(MinetopiaPlayer player, OwnableColorType type) {
        CompletableFuture<OwnableColor> colorFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getActivePrefixColorId,
                null
        ).thenAccept(integer -> StormUtils.getModelData(ColorModel.class,
                query -> query.where("id", Where.EQUAL, integer).where("type", Where.EQUAL, "prefix"),
                null,
                colorModel -> switch (type) {
                    case PREFIX ->
                            new PrefixColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                    case CHAT -> new ChatColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                    case NAME -> new NameColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                    case LEVEL -> new LevelColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                },
                null
        ).whenComplete((color, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                colorFuture.completeExceptionally(ex);
            } else {
                colorFuture.complete(color);
            }
        }));

        return colorFuture;
    }

    @Override
    public CompletableFuture<List<OwnableColor>> getColors(MinetopiaPlayer player) {
        CompletableFuture<List<OwnableColor>> completableFuture = new CompletableFuture<>();

        findColors(player).thenAccept(prefixesModels -> {
            // Create a list to store the colors
            List<OwnableColor> colors = new ArrayList<>();
            for (ColorModel colorModel : prefixesModels) {
                switch (colorModel.getType()) {
                    case "prefix":
                        colors.add(new PrefixColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                    case "chat":
                        colors.add(new ChatColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                    case "name":
                        colors.add(new NameColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                    case "level":
                        colors.add(new LevelColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                }
            }
            completableFuture.complete(colors);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<List<OwnableColor>> getColors(MinetopiaPlayer player, OwnableColorType type) {
        CompletableFuture<List<OwnableColor>> completableFuture = new CompletableFuture<>();

        findColors(player).thenAccept(prefixesModels -> {
            // Create a list to store the colors
            List<OwnableColor> colors = new ArrayList<>();
            for (ColorModel colorModel : prefixesModels) {
                switch (type) {
                    case PREFIX:
                        colors.add(new PrefixColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                    case CHAT:
                        colors.add(new ChatColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                    case NAME:
                        colors.add(new NameColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                    case LEVEL:
                        colors.add(new LevelColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
                        break;
                }
            }
            completableFuture.complete(colors);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    private CompletableFuture<List<ColorModel>> findColors(MinetopiaPlayer player) {
        CompletableFuture<List<ColorModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<ColorModel> colorModels = StormDatabase.getInstance().getStorm().buildQuery(ColorModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join();

                completableFuture.complete(new ArrayList<>(colorModels));
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }

    /* Level related database queries */

    @Override
    public CompletableFuture<Integer> getLevel(MinetopiaPlayer player) {
        CompletableFuture<Integer> levelFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getLevel,
                0
        ).whenComplete((level, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                levelFuture.completeExceptionally(ex);
            } else {
                levelFuture.complete(level);
            }
        });

        return levelFuture;  // Return the CompletableFuture
    }

    @Override
    public CompletableFuture<Void> setLevel(MinetopiaPlayer player, int level) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> playerModel.setLevel(level)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    /* World related database queries */

    @Override
    public CompletableFuture<Void> createWorld(MTWorld world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            WorldModel worldModel = new WorldModel();
            worldModel.setWorldName(world.getName());
            worldModel.setTitle(world.getTitle());
            worldModel.setTemperature(world.getTemperature());
            worldModel.setColor(world.getColor());
            worldModel.setLoadingName(world.getLoadingName());

            StormDatabase.getInstance().saveStormModel(worldModel);
            completableFuture.complete(null);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> deleteWorld(MTWorld world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.deleteModelData(WorldModel.class,
                query -> query.where("world_name", Where.EQUAL, world.getName())
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setTitle(MTWorld world, String title) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(WorldModel.class,
                query -> query.where("world_name", Where.EQUAL, world.getName()),
                worldModel -> worldModel.setTitle(title)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setTemperature(MTWorld world, double temperature) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(WorldModel.class,
                query -> query.where("world_name", Where.EQUAL, world.getName()),
                worldModel -> worldModel.setTemperature(temperature)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setColor(MTWorld world, String color) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(WorldModel.class,
                query -> query.where("world_name", Where.EQUAL, world.getName()),
                worldModel -> worldModel.setColor(color)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setLoadingName(MTWorld world, String loadingName) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(WorldModel.class,
                query -> query.where("world_name", Where.EQUAL, world.getName()),
                worldModel -> worldModel.setLoadingName(loadingName)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    /* City related database queries */

    @Override
    public CompletableFuture<Void> createCity(MTCity city) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            CityModel cityModel = new CityModel();
            cityModel.setCityName(city.getName());
            cityModel.setWorldId(city.getWorldId());
            cityModel.setTitle(city.getTitle());
            cityModel.setTemperature(city.getTemperature());
            cityModel.setColor(city.getColor());
            cityModel.setLoadingName(city.getLoadingName());

            StormDatabase.getInstance().saveStormModel(cityModel);
            completableFuture.complete(null);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> deleteCity(MTCity city) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.deleteModelData(CityModel.class,
                query -> query.where("city_name", Where.EQUAL, city.getName())
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setTitle(MTCity city, String title) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(CityModel.class,
                query -> query.where("city_name", Where.EQUAL, city.getName()),
                cityModel -> cityModel.setTitle(title)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setTemperature(MTCity city, double temperature) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(CityModel.class,
                query -> query.where("city_name", Where.EQUAL, city.getName()),
                cityModel -> cityModel.setTemperature(temperature)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setColor(MTCity city, String color) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(CityModel.class,
                query -> query.where("city_name", Where.EQUAL, city.getName()),
                cityModel -> cityModel.setColor(color)
        );
        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setLoadingName(MTCity city, String loadingName) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(CityModel.class,
                query -> query.where("city_name", Where.EQUAL, city.getName()),
                cityModel -> cityModel.setLoadingName(loadingName)
        );
        completableFuture.complete(null);
        return completableFuture;
    }
}