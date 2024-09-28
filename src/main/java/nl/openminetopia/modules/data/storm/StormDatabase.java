package nl.openminetopia.modules.data.storm;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.builders.QueryBuilder;
import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.models.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Setter
public class StormDatabase {
    private static StormDatabase instance;
    private @Getter Storm storm;
    private final static @Getter ExecutorService executorService = Executors.newFixedThreadPool(10);

    private StormDatabase() {
        // Private constructor to prevent instantiation
    }

    public static StormDatabase getInstance() {
        if (instance == null) {
            instance = new StormDatabase();
        }
        return instance;
    }

    public CompletableFuture<Optional<PlayerModel>> findPlayerModel(@NotNull UUID uuid) {
        CompletableFuture<Optional<PlayerModel>> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                Collection<PlayerModel> playerModel;
                playerModel = storm.buildQuery(PlayerModel.class).where("uuid", Where.EQUAL, uuid.toString()).limit(1).execute().join();
                Bukkit.getScheduler().runTaskLaterAsynchronously(OpenMinetopia.getInstance(), () -> completableFuture.complete(playerModel.stream().findFirst()), 1L);
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }

    public CompletableFuture<PlayerModel> loadPlayerModel(UUID uuid) {
        CompletableFuture<PlayerModel> completableFuture = new CompletableFuture<>();
        StormDatabase.getInstance().findPlayerModel(uuid).thenAccept(playerModel -> {
            PlayerManager.getInstance().getPlayerModels().remove(uuid);

            if (playerModel.isEmpty()) {
                PlayerModel createdModel = new PlayerModel();
                createdModel.setUniqueId(uuid);
                createdModel.setLevel(1);

                PlayerManager.getInstance().getPlayerModels().put(uuid, createdModel);
                completableFuture.complete(createdModel);

                saveStormModel(createdModel);
                return;
            }

            PlayerManager.getInstance().getPlayerModels().put(uuid, playerModel.get());
            completableFuture.complete(playerModel.get());
        });

        return completableFuture;
    }

    public CompletableFuture<Integer> saveStormModel(StormModel stormModel) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                completableFuture.complete(storm.save(stormModel));
            } catch (SQLException exception) {
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    /**
     * Doing something very ugly here, but it's for the greater good. TM
     */
    public <T extends StormModel> void updatePlayerRelatedModel(MinetopiaPlayer player, Class<T> modelClass, Consumer<T> updateAction) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                T model = StormDatabase.getInstance().getStorm().buildQuery(modelClass).where("uuid", Where.EQUAL, player.getUuid().toString()).execute().join().stream().findFirst().orElse(null);

                if (modelClass == PlayerModel.class) {
                    model = modelClass.cast(player.getPlayerModel());
                }

                if (model == null) {
                    // Create a new model instance if needed
                    if (modelClass == FitnessModel.class) {
                        model = modelClass.cast(new FitnessModel());
                        ((FitnessModel) model).setUniqueId(player.getUuid());
                    } else if (modelClass == PlayerModel.class) {
                        model = modelClass.cast(new PlayerModel());
                        ((PlayerModel) model).setUniqueId(player.getUuid());
                    } else if (modelClass == PrefixModel.class) {
                        model = modelClass.cast(new PrefixModel());
                        ((PrefixModel) model).setUniqueId(player.getUuid());
                    } else if (modelClass == ColorModel.class) {
                        model = modelClass.cast(new ColorModel());
                        ((ColorModel) model).setUniqueId(player.getUuid());
                    } else if (modelClass == FitnessBoosterModel.class) {
                        model = modelClass.cast(new PrefixModel());
                        ((PrefixModel) model).setUniqueId(player.getUuid());
                    }
                }

                // Apply the update
                updateAction.accept(model);

                // Save the model
                StormDatabase.getInstance().saveStormModel(model);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }


    /**
     * This method is used to fetch data from a model in the database.
     * It will return a CompletableFuture that will be completed with the data from the model.
     *
     * @param player The player to fetch the data for
     * @param modelClass The class of the model to fetch
     * @param filterBuilder A consumer that can be used to apply additional filters to the query
     *                      (e.g. query -> query.where("some_column", Where.EQUAL, someValue))
     * @param dataExtractor A function that extracts the data from the model, this may be used to filter through the stream
     *                      (e.g. model -> model.getSomeData())
     * @param defaultValue The default value to return if the model is not found
     *
     */
    public <T, M extends StormModel> CompletableFuture<T> getPlayerRelatedModelData(MinetopiaPlayer player,
                                                                                    Class<M> modelClass,
                                                                                    Consumer<QueryBuilder<M>> filterBuilder,
                                                                                    Predicate<M> postQueryFilter,  // New parameter to filter the stream
                                                                                    Function<M, T> dataExtractor,
                                                                                    T defaultValue) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                QueryBuilder<M> query = StormDatabase.getInstance().getStorm().buildQuery(modelClass)
                        .where("uuid", Where.EQUAL, player.getUuid().toString());

                // Apply additional query filters via filterBuilder
                filterBuilder.accept(query);

                M model = query.execute()
                        .join()
                        .stream()
                        .filter(postQueryFilter) // Apply the stream filter after fetching the data
                        .findFirst()
                        .orElse(null);

                if (model != null) {
                    completableFuture.complete(dataExtractor.apply(model));
                } else {
                    completableFuture.complete(defaultValue);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }


    public <M extends StormModel> void deletePlayerRelatedModel(MinetopiaPlayer player, Class<M> modelClass, Predicate<M> deleteCondition) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                M model = StormDatabase.getInstance().getStorm().buildQuery(modelClass).where("uuid", Where.EQUAL, player.getUuid().toString()).execute().join().stream().filter(deleteCondition).findFirst().orElse(null);

                if (model != null) {
                    StormDatabase.getInstance().getStorm().delete(model);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}