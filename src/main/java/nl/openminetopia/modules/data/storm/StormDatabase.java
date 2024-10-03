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

    public CompletableFuture<Integer> saveStormModel(StormModel stormModel) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                completableFuture.complete(storm.save(stormModel));
            } catch (SQLException exception) {
                OpenMinetopia.getInstance().getLogger().severe("Something went wrong! " + exception.getMessage());
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }
}