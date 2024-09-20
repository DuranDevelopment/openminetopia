package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.ColorsModel;
import nl.openminetopia.modules.data.storm.models.PlayerModel;

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

    /**
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

        findPlayerColors(player, OwnableColorType.PREFIX).thenAccept(prefixColorsModels -> {
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

    public CompletableFuture<PrefixColor> getPlayerActivePrefixColor(MinetopiaPlayer player) {
        CompletableFuture<PrefixColor> result = new CompletableFuture<>();

        StormDatabase.getInstance().getModelData(player, PlayerModel.class,
                        query -> {},
                        playerModel -> true,
                        PlayerModel::getActivePrefixColorId, 0)
                .whenComplete((activePrefixId, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                        result.completeExceptionally(ex); // Handle exception
                        return;
                    }

                    StormDatabase.getInstance().getModelData(player,
                                    ColorsModel.class,
                                    query -> query.where("id", Where.EQUAL, activePrefixId).where("type", Where.EQUAL, "prefix"),
                                    colorsModel -> colorsModel.getExpiresAt() > System.currentTimeMillis() || colorsModel.getExpiresAt() == -1,
                                    colorsModel -> new PrefixColor(colorsModel.getId(), colorsModel.getColor(), colorsModel.getExpiresAt()),
                                    null)
                            .whenComplete((prefixColor, ex2) -> {
                                if (ex2 != null) {
                                    ex2.printStackTrace();
                                    result.completeExceptionally(ex2); // Handle exception
                                } else {
                                    result.complete(prefixColor); // Complete with the retrieved prefix color
                                }
                            });
                });

        return result;
    }

    /**
     * Other methods
     */

    private CompletableFuture<List<ColorsModel>> findPlayerColors(MinetopiaPlayer player, OwnableColorType type) {
        CompletableFuture<List<ColorsModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<ColorsModel> colorsModels = StormDatabase.getInstance().getStorm().buildQuery(ColorsModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .where("type", Where.EQUAL, type.name().toLowerCase())
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
}
