package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.ColorModel;
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
        StormDatabase.getInstance().updateModel(player, ColorModel.class, colorModel -> {
            colorModel.setUniqueId(player.getUuid());
            colorModel.setColor(color.getColor());
            colorModel.setType("prefix");
            colorModel.setExpiresAt(color.getExpiresAt());
        });
    }

    public void removePrefixColor(MinetopiaPlayer player, PrefixColor color) {
        StormDatabase.getInstance().deleteModel(player, ColorModel.class, colorModel -> colorModel.getId() == color.getId());
    }

    public void setActivePrefixColor(MinetopiaPlayer player, PrefixColor color) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setActivePrefixColorId(color.getId()));
    }

    public CompletableFuture<List<PrefixColor>> getPrefixColors(MinetopiaPlayer player) {
        CompletableFuture<List<PrefixColor>> completableFuture = new CompletableFuture<>();

        findPlayerColors(player, OwnableColorType.PREFIX).thenAccept(prefixColorsModels -> {
            List<PrefixColor> prefixColors = new ArrayList<>();
            for (ColorModel colorModel : prefixColorsModels) {
                prefixColors.add(new PrefixColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()));
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
                                    ColorModel.class,
                                    query -> query.where("id", Where.EQUAL, activePrefixId).where("type", Where.EQUAL, "prefix"),
                                    colorModel -> colorModel.getExpiresAt() > System.currentTimeMillis() || colorModel.getExpiresAt() == -1,
                                    colorModel -> new PrefixColor(colorModel.getId(), colorModel.getColor(), colorModel.getExpiresAt()),
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

    private CompletableFuture<List<ColorModel>> findPlayerColors(MinetopiaPlayer player, OwnableColorType type) {
        CompletableFuture<List<ColorModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<ColorModel> colorModels = StormDatabase.getInstance().getStorm().buildQuery(ColorModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .where("type", Where.EQUAL, type.name().toLowerCase())
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
}
