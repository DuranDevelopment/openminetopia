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
}