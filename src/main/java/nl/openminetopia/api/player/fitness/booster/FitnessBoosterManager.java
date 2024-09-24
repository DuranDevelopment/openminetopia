package nl.openminetopia.api.player.fitness.booster;

import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessBoosterModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class FitnessBoosterManager {

    private static FitnessBoosterManager instance;

    public static FitnessBoosterManager getInstance() {
        if (instance == null) {
            instance = new FitnessBoosterManager();
        }
        return instance;
    }

    public void addFitnessBooster(MinetopiaPlayer player, FitnessBooster booster) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessBoosterModel fitnessBoosterModel = new FitnessBoosterModel();
                fitnessBoosterModel.setUniqueId(player.getUuid());
                fitnessBoosterModel.setFitness(booster.getAmount());
                fitnessBoosterModel.setExpiresAt(booster.getExpiresAt());

                StormDatabase.getInstance().saveStormModel(fitnessBoosterModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void removeFitnessBooster(MinetopiaPlayer player, FitnessBooster booster) {
        StormDatabase.getInstance().deleteModel(player, FitnessBoosterModel.class, model -> model.getId() == booster.getId());
    }

    public CompletableFuture<List<FitnessBooster>> getFitnessBoosters(MinetopiaPlayer player) {
        CompletableFuture<List<FitnessBooster>> completableFuture = new CompletableFuture<>();

        findPlayerFitnessBoosts(player).thenAccept(fitnessBoosters -> {
            List<FitnessBooster> prefixes = new ArrayList<>();
            for (FitnessBoosterModel fitnessBoosterModel : fitnessBoosters) {
                prefixes.add(new FitnessBooster(fitnessBoosterModel.getId(), fitnessBoosterModel.getFitness(), fitnessBoosterModel.getExpiresAt()));
            }
            completableFuture.complete(prefixes);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    private CompletableFuture<List<FitnessBoosterModel>> findPlayerFitnessBoosts(MinetopiaPlayer player) {
        CompletableFuture<List<FitnessBoosterModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<FitnessBoosterModel> prefixesModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessBoosterModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join();

                completableFuture.complete(new ArrayList<>(prefixesModel));
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }
}
