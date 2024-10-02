package nl.openminetopia.modules.data.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.builders.QueryBuilder;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.hikaricp.HikariDriver;
import com.zaxxer.hikari.HikariConfig;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.banking.enums.AccountType;
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
import java.util.stream.Collectors;

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
            config.setMaximumPoolSize(16);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            StormDatabase.getInstance().setStorm(new Storm(new HikariDriver(config)));
        } catch (Exception e) {
            OpenMinetopia.getInstance().getLogger().severe("Failed to connect to MySQL database: " + e.getMessage());
            OpenMinetopia.getInstance().getLogger().severe("Disabling the plugin...");
            OpenMinetopia.getInstance().getServer().getPluginManager().disablePlugin(OpenMinetopia.getInstance());
        }
    }

    @Override
    public void disconnect() {
        // Do nothing, Storm handles this for us
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
        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                model -> {
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
                }
        );
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
    public CompletableFuture<Boolean> getStaffchatEnabled(MinetopiaPlayer player) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getStaffchatEnabled,
                false
        ).whenComplete((staffchatEnabled, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                completableFuture.completeExceptionally(ex);
                return;
            }
            completableFuture.complete(staffchatEnabled);
        });
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

    @Override
    public CompletableFuture<Boolean> setCommandSpyEnabled(MinetopiaPlayer player, boolean enabled) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> playerModel.setCommandSpyEnabled(enabled)
        );
        completableFuture.complete(enabled);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> setChatSpyEnabled(MinetopiaPlayer player, boolean enabled) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        StormUtils.updateModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                playerModel -> playerModel.setChatSpyEnabled(enabled)
        );
        completableFuture.complete(enabled);
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> getCommandSpyEnabled(MinetopiaPlayer player) {
        CompletableFuture<Boolean> spyFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getCommandSpyEnabled,
                false
        ).whenComplete((spy, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                spyFuture.completeExceptionally(ex);
                return;
            }
            spyFuture.complete(spy);
        });
        return spyFuture;
    }

    @Override
    public CompletableFuture<Boolean> getChatSpyEnabled(MinetopiaPlayer player) {
        CompletableFuture<Boolean> spyFuture = new CompletableFuture<>();

        StormUtils.getModelData(PlayerModel.class,
                query -> query.where("uuid", Where.EQUAL, player.getUuid().toString()),
                null,
                PlayerModel::getChatSpyEnabled,
                false
        ).whenComplete((spy, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                spyFuture.completeExceptionally(ex);
                return;
            }
            spyFuture.complete(spy);
        });
        return spyFuture;
    }

    /* Prefix related database queries */

    @Override
    public CompletableFuture<Void> addPrefix(MinetopiaPlayer player, Prefix prefix) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            PrefixModel prefixModel = new PrefixModel();
            prefixModel.setPlayerId(player.getPlayerModel().getId());
            prefixModel.setPrefix(prefix.getPrefix());
            prefixModel.setExpiresAt(prefix.getExpiresAt());

            StormDatabase.getInstance().saveStormModel(prefixModel);
            completableFuture.complete(null);
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> removePrefix(MinetopiaPlayer player, Prefix prefix) {
        return StormUtils.deleteModelData(PrefixModel.class,
                query -> query.where("id", Where.EQUAL, prefix.getId())
        );
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

        StormDatabase.getExecutorService().submit(() -> {
            List<Prefix> prefixes = player.getPlayerModel().getPrefixes().stream().map(prefixModel -> new Prefix(prefixModel.getId(), prefixModel.getPrefix(), prefixModel.getExpiresAt()))
                    .collect(Collectors.toList());

            completableFuture.complete(prefixes);
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

    /* Color related database queries */

    @Override
    public CompletableFuture<Void> addColor(MinetopiaPlayer player, OwnableColor color) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            ColorModel colorModel = new ColorModel();
            colorModel.setPlayerId(player.getPlayerModel().getId());
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
        return StormUtils.deleteModelData(ColorModel.class,
                query -> query.where("id", Where.EQUAL, color.getId())
        );
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

        StormDatabase.getExecutorService().submit(() -> {
            List<OwnableColor> colors = player.getPlayerModel().getColors().stream().map(colorModel -> switch (colorModel.getType()) {
                case "prefix" -> new PrefixColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                case "name" -> new NameColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                case "chat" -> new ChatColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                case "level" -> new LevelColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt());
                default -> null;
            }).collect(Collectors.toList());
            completableFuture.complete(colors);
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
    public CompletableFuture<WorldModel> createWorld(MTWorld world) {
        CompletableFuture<WorldModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            WorldModel worldModel = new WorldModel();
            worldModel.setWorldName(world.getName());
            worldModel.setTitle(world.getTitle());
            worldModel.setTemperature(world.getTemperature());
            worldModel.setColor(world.getColor());
            worldModel.setLoadingName(world.getLoadingName());

            StormDatabase.getInstance().saveStormModel(worldModel);
            completableFuture.complete(worldModel);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> deleteWorld(MTWorld world) {
        return StormUtils.deleteModelData(WorldModel.class,
                query -> query.where("world_name", Where.EQUAL, world.getName())
        );
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
    public CompletableFuture<CityModel> createCity(MTCity city) {
        CompletableFuture<CityModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            CityModel cityModel = new CityModel();
            cityModel.setCityName(city.getName());
            cityModel.setWorldId(city.getWorldId());
            cityModel.setTitle(city.getTitle());
            cityModel.setTemperature(city.getTemperature());
            cityModel.setColor(city.getColor());
            cityModel.setLoadingName(city.getLoadingName());

            StormDatabase.getInstance().saveStormModel(cityModel);
            completableFuture.complete(cityModel);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> deleteCity(MTCity city) {
        return StormUtils.deleteModelData(CityModel.class,
                query -> query.where("city_name", Where.EQUAL, city.getName())
        );
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

    /* Fitness related database queries */

    public CompletableFuture<FitnessModel> getFitness(Fitness fitness) {
        CompletableFuture<FitnessModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            QueryBuilder<FitnessModel> query = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class);

            try {
                CompletableFuture<Collection<FitnessModel>> models = query.where("uuid", Where.EQUAL, fitness.getUuid().toString()).execute();

                models.whenComplete((fitnessModels, throwable) -> {
                    FitnessModel model = fitnessModels.stream().findFirst().orElse(null);
                    if (model == null) {
                        model = new FitnessModel();
                        model.setUniqueId(fitness.getUuid());
                        model.setTotal(0);
                        model.setFitnessGainedByWalking(0);
                        model.setFitnessGainedByDrinking(0);
                        model.setDrinkingPoints(0.0);
                        model.setFitnessGainedBySprinting(0);
                        model.setFitnessGainedByClimbing(0);
                        model.setFitnessGainedBySwimming(0);
                        model.setFitnessGainedByFlying(0);
                        model.setFitnessGainedByHealth(0);
                        model.setHealthPoints(0);
                        model.setFitnessGainedByEating(0);
                        model.setLuxuryFood(0);
                        model.setCheapFood(0);
                        StormDatabase.getInstance().saveStormModel(model);
                    }
                    saveStatistics(fitness);
                    completableFuture.complete(model);
                });
            } catch (Exception e) {
                e.printStackTrace();
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> saveStatistics(Fitness fitness) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            getFitness(fitness).thenAccept(model -> {
                        model.setTotal((fitness.getStatistic(FitnessStatisticType.TOTAL).getFitnessGained()));
                        model.setFitnessGainedByWalking(fitness.getStatistic(FitnessStatisticType.WALKING).getFitnessGained());
                        model.setFitnessGainedBySprinting(fitness.getStatistic(FitnessStatisticType.SPRINTING).getFitnessGained());
                        model.setFitnessGainedByClimbing(fitness.getStatistic(FitnessStatisticType.CLIMBING).getFitnessGained());
                        model.setFitnessGainedBySwimming(fitness.getStatistic(FitnessStatisticType.SWIMMING).getFitnessGained());
                        model.setFitnessGainedByFlying(fitness.getStatistic(FitnessStatisticType.FLYING).getFitnessGained());

                        DrinkingStatistic drinkingStatistic = (DrinkingStatistic) fitness.getStatistic(FitnessStatisticType.DRINKING);
                        model.setFitnessGainedByDrinking(drinkingStatistic.getFitnessGained());
                        model.setDrinkingPoints(drinkingStatistic.getPoints());

                        HealthStatistic healthStatistic = (HealthStatistic) fitness.getStatistic(FitnessStatisticType.HEALTH);
                        model.setFitnessGainedByHealth(healthStatistic.getFitnessGained());
                        model.setHealthPoints(healthStatistic.getPoints());

                        EatingStatistic eatingStatistic = (EatingStatistic) fitness.getStatistic(FitnessStatisticType.EATING);
                        model.setFitnessGainedByEating(eatingStatistic.getFitnessGained());
                        model.setLuxuryFood(eatingStatistic.getLuxuryFood());
                        model.setCheapFood(eatingStatistic.getCheapFood());
                        StormDatabase.getInstance().saveStormModel(model);
                    }
            );
        });

        completableFuture.complete(null);
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<FitnessStatistic>> getStatistics(Fitness fitness) {
        CompletableFuture<List<FitnessStatistic>> completableFuture = new CompletableFuture<>();

        StormUtils.getModelData(FitnessModel.class, query -> query.where("uuid", Where.EQUAL, fitness.getUuid().toString()), null,
                model -> {
                    List<FitnessStatistic> stats = new ArrayList<>();

                    stats.add(new TotalStatistic(model.getTotal()));
                    stats.add(new WalkingStatistic(model.getFitnessGainedByWalking()));
                    stats.add(new DrinkingStatistic(model.getFitnessGainedByDrinking(), model.getHealthPoints()));
                    stats.add(new SprintingStatistic(model.getFitnessGainedBySprinting()));
                    stats.add(new ClimbingStatistic(model.getFitnessGainedByClimbing()));
                    stats.add(new SwimmingStatistic(model.getFitnessGainedBySwimming()));
                    stats.add(new FlyingStatistic(model.getFitnessGainedByFlying()));
                    stats.add(new HealthStatistic(model.getFitnessGainedByHealth(), model.getHealthPoints()));
                    stats.add(new EatingStatistic(model.getFitnessGainedByEating(), model.getLuxuryFood(), model.getCheapFood()));

                    completableFuture.complete(stats);
                    return completableFuture;
                },
                List.of()
        );

        return completableFuture;
    }

    @Override
    public CompletableFuture<FitnessStatistic> getStatistic(Fitness fitness, FitnessStatisticType type) {
        CompletableFuture<FitnessStatistic> completableFuture = new CompletableFuture<>();

        StormUtils.getModelData(FitnessModel.class,
                query -> query.where("uuid", Where.EQUAL, fitness.getUuid().toString()),
                null,
                model -> switch (type) {
                    case TOTAL -> new TotalStatistic(model.getTotal());
                    case WALKING -> new WalkingStatistic(model.getFitnessGainedByWalking());
                    case DRINKING -> new DrinkingStatistic(model.getFitnessGainedByDrinking(), model.getHealthPoints());
                    case SPRINTING -> new SprintingStatistic(model.getFitnessGainedBySprinting());
                    case CLIMBING -> new ClimbingStatistic(model.getFitnessGainedByClimbing());
                    case SWIMMING -> new SwimmingStatistic(model.getFitnessGainedBySwimming());
                    case FLYING -> new FlyingStatistic(model.getFitnessGainedByFlying());
                    case HEALTH -> new HealthStatistic(model.getFitnessGainedByHealth(), model.getHealthPoints());
                    case EATING ->
                            new EatingStatistic(model.getFitnessGainedByEating(), model.getLuxuryFood(), model.getCheapFood());
                },
                null
        ).whenComplete((statistic, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                completableFuture.completeExceptionally(ex);
                return;
            }
            completableFuture.complete(statistic);
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> saveFitnessBoosters(Fitness fitness) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            fitness.getBoosters().forEach(booster -> {
                FitnessBoosterModel model = new FitnessBoosterModel();
                model.setFitnessId(fitness.getFitnessModel().getId());
                model.setFitness(booster.getAmount());
                model.setExpiresAt(booster.getExpiresAt());

                StormDatabase.getInstance().saveStormModel(model);
            });
            completableFuture.complete(null);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<List<FitnessBooster>> getFitnessBoosters(Fitness fitness) {
        CompletableFuture<List<FitnessBooster>> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            List<FitnessBooster> fitnessBoosters = fitness.getFitnessModel().getBoosters().stream().map(model -> new FitnessBooster(model.getId(), model.getFitness(), model.getExpiresAt())).collect(Collectors.toList());

            completableFuture.complete(fitnessBoosters);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<FitnessBoosterModel> addFitnessBooster(Fitness fitness, FitnessBooster booster) {
        CompletableFuture<FitnessBoosterModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            FitnessBoosterModel fitnessBoosterModel = new FitnessBoosterModel();
            fitnessBoosterModel.setFitnessId(fitness.getFitnessModel().getId());
            fitnessBoosterModel.setFitness(booster.getAmount());
            fitnessBoosterModel.setExpiresAt(booster.getExpiresAt());

            StormDatabase.getInstance().saveStormModel(fitnessBoosterModel);
            completableFuture.complete(fitnessBoosterModel);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> removeFitnessBooster(Fitness fitness, FitnessBooster booster) {
        return StormUtils.deleteModelData(FitnessBoosterModel.class,
                query -> query.where("id", Where.EQUAL, booster.getId())
        );
    }

    @Override
    public CompletableFuture<BankAccountModel> createBankAccount(UUID uuid, AccountType type, long balance, String name, boolean frozen) {
        CompletableFuture<BankAccountModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            BankAccountModel accountModel = new BankAccountModel();
            accountModel.setUniqueId(uuid);
            accountModel.setType(type);
            accountModel.setBalance(balance);
            accountModel.setName(name);
            accountModel.setFrozen(frozen);

            StormDatabase.getInstance().saveStormModel(accountModel);
            completableFuture.complete(accountModel);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> deleteBankAccount(UUID accountUuid) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            StormUtils.deleteModelData(BankAccountModel.class,
                    query -> query.where("uuid", Where.EQUAL, accountUuid.toString())
            ).join();

            StormUtils.deleteModelData(BankPermissionModel.class,
                    query -> query.where("account", Where.EQUAL, accountUuid.toString())
            ).join();

            completableFuture.complete(null);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<BankPermissionModel> createBankPermission(UUID player, UUID accountId, AccountPermission permission) {
        CompletableFuture<BankPermissionModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            BankPermissionModel permissionModel = new BankPermissionModel();
            permissionModel.setUuid(player);
            permissionModel.setAccount(accountId);
            permissionModel.setPermission(permission);

            StormDatabase.getInstance().saveStormModel(permissionModel);
            completableFuture.complete(permissionModel);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> deleteBankPermission(UUID accountUuid, UUID playerUuid) {
        return StormUtils.deleteModelData(BankPermissionModel.class, query -> {
            query.where("uuid", Where.EQUAL, playerUuid.toString());
            query.where("account", Where.EQUAL, accountUuid.toString());
        });
    }
}