package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.storm.models.PrefixColorsModel;
import nl.openminetopia.modules.data.storm.models.PrefixesModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.modules.prefix.objects.PrefixColor;

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
     * Prefix colors
     */
    public void addPrefixColor(MinetopiaPlayer player, PrefixColor color, long expiresAt) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PrefixColorsModel prefixColorsModel = new PrefixColorsModel();
                prefixColorsModel.setUniqueId(player.getUuid());
                prefixColorsModel.setColor(color.getColor());
                prefixColorsModel.setExpiresAt(expiresAt);

                StormDatabase.getInstance().saveStormModel(prefixColorsModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void removePrefixColor(MinetopiaPlayer player, PrefixColor color) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PrefixColorsModel prefixColorsModel = StormDatabase.getInstance().getStorm().buildQuery(PrefixColorsModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .where("id", Where.EQUAL, color.getId())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                StormDatabase.getInstance().getStorm().delete(prefixColorsModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setActivePrefixColorId(MinetopiaPlayer player, int id) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PlayerModel playerModel = StormDatabase.getInstance().getStorm().buildQuery(PlayerModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (playerModel != null) {
                    playerModel.setActivePrefixColorId(id);
                    StormDatabase.getInstance().saveStormModel(playerModel);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public CompletableFuture<List<PrefixColor>> getPrefixColors(MinetopiaPlayer player) {
        CompletableFuture<List<PrefixColor>> completableFuture = new CompletableFuture<>();

        findPlayerPrefixColors(player).thenAccept(prefixColorsModels -> {
            // Create a list to store the prefixes
            List<PrefixColor> prefixColors = new ArrayList<>();
            for (PrefixColorsModel prefixColorsModel : prefixColorsModels) {
                prefixColors.add(new PrefixColor(prefixColorsModel.getId(), prefixColorsModel.getColor(), prefixColorsModel.getExpiresAt()));
            }
            completableFuture.complete(prefixColors);
        }).exceptionally(ex -> {
            completableFuture.completeExceptionally(ex);
            return null;
        });

        return completableFuture;
    }

    private CompletableFuture<List<PrefixColorsModel>> findPlayerPrefixColors(MinetopiaPlayer player) {
        CompletableFuture<List<PrefixColorsModel>> completableFuture = new CompletableFuture<>();
        StormDatabase.getExecutorService().submit(() -> {
            try {
                Collection<PrefixColorsModel> prefixColorsModels = StormDatabase.getInstance().getStorm().buildQuery(PrefixColorsModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join();

                completableFuture.complete(new ArrayList<>(prefixColorsModels));
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }

    /*
     * Prefixes
     */

    public void addPrefix(MinetopiaPlayer player, String prefix, long expiresAt) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PrefixesModel prefixesModel = new PrefixesModel();
                prefixesModel.setUniqueId(player.getUuid());
                prefixesModel.setPrefix(prefix);
                prefixesModel.setExpiresAt(expiresAt);

                StormDatabase.getInstance().saveStormModel(prefixesModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setActivePrefixId(MinetopiaPlayer player, int id) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PlayerModel playerModel = StormDatabase.getInstance().getStorm().buildQuery(PlayerModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (playerModel != null) {
                    playerModel.setActivePrefixId(id);
                    StormDatabase.getInstance().saveStormModel(playerModel);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void removePrefix(MinetopiaPlayer player, Prefix prefix) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                PrefixesModel prefixesModel = StormDatabase.getInstance().getStorm().buildQuery(PrefixesModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .where("id", Where.EQUAL, prefix.getId())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                StormDatabase.getInstance().getStorm().delete(prefixesModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public CompletableFuture<Prefix> getPlayerActivePrefix(MinetopiaPlayer player) {
        CompletableFuture<Prefix> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                PrefixesModel prefixesModel = StormDatabase.getInstance().getStorm().buildQuery(PrefixesModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .filter(prefixesModel2 -> prefixesModel2.getExpiresAt() > System.currentTimeMillis() || prefixesModel2.getExpiresAt() == -1)
                        .findFirst()
                        .orElse(null);

                if (prefixesModel != null) {
                    completableFuture.complete(new Prefix(prefixesModel.getId(), prefixesModel.getPrefix(), prefixesModel.getExpiresAt()));
                } else {
                    completableFuture.complete(null);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public int getNextId() {
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
