package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.storm.models.ColorsModel;
import nl.openminetopia.modules.data.storm.models.PrefixesModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.modules.color.objects.PrefixColor;

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
                PrefixesModel prefixesModel = new PrefixesModel();
                prefixesModel.setUniqueId(player.getUuid());
                prefixesModel.setPrefix(prefix.getPrefix());
                prefixesModel.setExpiresAt(prefix.getExpiresAt());

                StormDatabase.getInstance().saveStormModel(prefixesModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setActivePrefixId(MinetopiaPlayer player, int id) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setActivePrefixId(id));
    }

    public void removePrefix(MinetopiaPlayer player, Prefix prefix) {
        StormDatabase.getInstance().deleteModel(player, PrefixesModel.class, prefixesModel -> prefixesModel.getId() == prefix.getId());
    }

    public CompletableFuture<Prefix> getPlayerActivePrefix(MinetopiaPlayer player) {
        try {
            int activePrefixId = StormDatabase.getInstance().getModelData(player,
                    PlayerModel.class,
                    query -> {
                    },
                    playerModel -> true,
                    PlayerModel::getActivePrefixId,
                    0).get();

            return StormDatabase.getInstance().getModelData(player,
                    PrefixesModel.class,
                    query -> query.where("id", Where.EQUAL, activePrefixId),
                    prefixesModel -> prefixesModel.getExpiresAt() > System.currentTimeMillis() || prefixesModel.getExpiresAt() == -1,
                    prefixesModel -> new Prefix(prefixesModel.getId(), prefixesModel.getPrefix(), prefixesModel.getExpiresAt()),
                    null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int getNextPrefixId() {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<PrefixesModel> prefixesModels = StormDatabase.getInstance().getStorm().buildQuery(PrefixesModel.class)
                        .execute()
                        .join();

                int id = 1;
                for (PrefixesModel prefixesModel : prefixesModels) {
                    if (prefixesModel.getId() >= id) {
                        id = prefixesModel.getId() + 1;
                    }
                }
                completableFuture.complete(id);
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture.join();
    }

    public CompletableFuture<List<Prefix>> getPrefixes(MinetopiaPlayer player) {
        CompletableFuture<List<Prefix>> completableFuture = new CompletableFuture<>();

        findPlayerPrefixes(player).thenAccept(prefixesModels -> {
            // Create a list to store the prefixes
            List<Prefix> prefixes = new ArrayList<>();
            for (PrefixesModel prefixesModel : prefixesModels) {
                prefixes.add(new Prefix(prefixesModel.getId(), prefixesModel.getPrefix(), prefixesModel.getExpiresAt()));
            }
            completableFuture.complete(prefixes);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    private CompletableFuture<List<PrefixesModel>> findPlayerPrefixes(MinetopiaPlayer player) {
        CompletableFuture<List<PrefixesModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<PrefixesModel> prefixesModel = StormDatabase.getInstance().getStorm().buildQuery(PrefixesModel.class)
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
