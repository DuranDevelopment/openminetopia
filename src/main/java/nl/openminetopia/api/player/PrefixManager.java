package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.storm.models.PrefixModel;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PrefixManager {

    private static PrefixManager instance;

    public static PrefixManager getInstance() {
        if (instance == null) {
            instance = new PrefixManager();
        }
        return instance;
    }

    /*
     * Prefixes
     */

    public void addPrefix(MinetopiaPlayer player, Prefix prefix) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PrefixModel prefixModel = new PrefixModel();
                prefixModel.setUniqueId(player.getUuid());
                prefixModel.setPrefix(prefix.getPrefix());
                prefixModel.setExpiresAt(prefix.getExpiresAt());

                StormDatabase.getInstance().saveStormModel(prefixModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setActivePrefixId(MinetopiaPlayer player, int id) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setActivePrefixId(id));
    }

    public void removePrefix(MinetopiaPlayer player, Prefix prefix) {
        StormDatabase.getInstance().deleteModel(player, PrefixModel.class, prefixModel -> prefixModel.getId() == prefix.getId());
    }

    public CompletableFuture<Prefix> getPlayerActivePrefix(MinetopiaPlayer player) {
        CompletableFuture<Prefix> result = new CompletableFuture<>();

        StormDatabase.getInstance().getModelData(player, PlayerModel.class,
                        query -> {},
                        playerModel -> true,
                        PlayerModel::getActivePrefixId, 0)
                .whenComplete((activePrefixId, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                        result.completeExceptionally(ex); // Handle exception in the result future
                        return;
                    }

                    StormDatabase.getInstance().getModelData(player,
                                    PrefixModel.class,
                                    query -> query.where("id", Where.EQUAL, activePrefixId),
                                    prefixModel -> prefixModel.getExpiresAt() > System.currentTimeMillis() || prefixModel.getExpiresAt() == -1,
                                    prefixModel -> new Prefix(prefixModel.getId(), prefixModel.getPrefix(), prefixModel.getExpiresAt()),
                                    null)
                            .whenComplete((prefix, ex2) -> {
                                if (ex2 != null) {
                                    ex2.printStackTrace();
                                    result.completeExceptionally(ex2); // Handle exception in the result future
                                } else {
                                    result.complete(prefix); // Complete with the retrieved prefix
                                }
                            });
                });
        return result;
    }

    public CompletableFuture<List<Prefix>> getPrefixes(MinetopiaPlayer player) {
        CompletableFuture<List<Prefix>> completableFuture = new CompletableFuture<>();

        findPlayerPrefixes(player).thenAccept(prefixesModels -> {
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

    private CompletableFuture<List<PrefixModel>> findPlayerPrefixes(MinetopiaPlayer player) {
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
}
