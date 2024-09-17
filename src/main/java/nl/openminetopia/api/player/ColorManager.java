package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.ColorsModel;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.storm.models.PrefixesModel;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColorManager {

    private static ColorManager instance;

    public static ColorManager getInstance() {
        if (instance == null) {
            instance = new ColorManager();
        }
        return instance;
    }

    /*
     * Prefix colors
     */
    public void addPrefixColor(MinetopiaPlayer player, PrefixColor color) {
        StormDatabase.getInstance().updateModel(player, ColorsModel.class, colorsModel -> {
            colorsModel.setUniqueId(player.getUuid());
            colorsModel.setColor(color.getColor());
            colorsModel.setType("prefix");
            colorsModel.setExpiresAt(color.getExpiresAt());
        });
    }

    public void removePrefixColor(MinetopiaPlayer player, PrefixColor color) {
        StormDatabase.getInstance().deleteModel(player, ColorsModel.class, colorsModel -> colorsModel.getId() == color.getId());
    }

    public void setActivePrefixColor(MinetopiaPlayer player, PrefixColor color) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setActivePrefixColorId(color.getId()));
    }

    public CompletableFuture<List<PrefixColor>> getPrefixColors(MinetopiaPlayer player) {
        CompletableFuture<List<PrefixColor>> completableFuture = new CompletableFuture<>();

        findPlayerPrefixColors(player).thenAccept(prefixColorsModels -> {
            // Create a list to store the prefixes
            List<PrefixColor> prefixColors = new ArrayList<>();
            for (ColorsModel colorsModel : prefixColorsModels) {
                prefixColors.add(new PrefixColor(colorsModel.getId(), colorsModel.getColor(), colorsModel.getExpiresAt()));
            }
            completableFuture.complete(prefixColors);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    private CompletableFuture<List<ColorsModel>> findPlayerPrefixColors(MinetopiaPlayer player) {
        CompletableFuture<List<ColorsModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<ColorsModel> colorsModels = StormDatabase.getInstance().getStorm().buildQuery(ColorsModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .where("type", Where.EQUAL, "prefix")
                        .execute()
                        .join();

                completableFuture.complete(new ArrayList<>(colorsModels));
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }

    public CompletableFuture<PrefixColor> getPlayerActivePrefixColor(MinetopiaPlayer player) {
        try {
            int activePrefixId = StormDatabase.getInstance().getModelData(player,
                    PlayerModel.class,
                    query -> {
                    },
                    playerModel -> true,
                    PlayerModel::getActivePrefixColorId,
                    0).get();

            return StormDatabase.getInstance().getModelData(player,
                    ColorsModel.class,
                    query -> query.where("id", Where.EQUAL, activePrefixId).where("type", Where.EQUAL, "prefix"),
                    colorsModel -> colorsModel.getExpiresAt() > System.currentTimeMillis() || colorsModel.getExpiresAt() == -1,
                    colorsModel -> new PrefixColor(colorsModel.getId(), colorsModel.getColor(), colorsModel.getExpiresAt()),
                    null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int getNextPrefixColorId() {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<ColorsModel> colorsModels = StormDatabase.getInstance().getStorm().buildQuery(ColorsModel.class)
                        .execute()
                        .join();

                int id = 1;
                for (ColorsModel colorsModel : colorsModels) {
                    if (colorsModel.getId() >= id) {
                        id = colorsModel.getId() + 1;
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
}
