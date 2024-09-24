package nl.openminetopia.api.player;

import lombok.Getter;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OfflineMinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class PlayerManager {

    private static PlayerManager instance;

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public HashMap<UUID, PlayerModel> playerModels = new HashMap<>();
    public HashMap<UUID, MinetopiaPlayer> minetopiaPlayers = new HashMap<>();

    public @Nullable MinetopiaPlayer getMinetopiaPlayer(@NotNull OfflinePlayer player) {

        UUID playerId = player.getUniqueId();
        MinetopiaPlayer minetopiaPlayer = minetopiaPlayers.get(playerId);

        if (minetopiaPlayer != null) {
            // If the player is online and the current instance is offline, update to online
            if (minetopiaPlayer instanceof OfflineMinetopiaPlayer && player.isOnline()) {
                minetopiaPlayer = new OnlineMinetopiaPlayer(playerId, playerModels.get(playerId));
            }
            // If the player is offline and the current instance is online, update to offline
            else if (minetopiaPlayer instanceof OnlineMinetopiaPlayer && !player.isOnline()) {
                minetopiaPlayer = new OfflineMinetopiaPlayer(playerId);
            }

            // Update the player in the map if we made a change
            minetopiaPlayers.put(playerId, minetopiaPlayer);
        } else {
            // If no player was found, create the correct instance based on online status
            if (player.isOnline()) {
                minetopiaPlayer = new OnlineMinetopiaPlayer(playerId, playerModels.get(playerId));
            } else {
                minetopiaPlayer = new OfflineMinetopiaPlayer(playerId);
            }

            minetopiaPlayers.put(playerId, minetopiaPlayer);
        }

        return minetopiaPlayer;
    }

    public void setPlaytime(@NotNull MinetopiaPlayer player, int playtime) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setPlaytime(playtime));
    }

    public CompletableFuture<Integer> getPlaytime(@NotNull MinetopiaPlayer player) {
        return StormDatabase.getInstance().getModelData(player, PlayerModel.class, query -> {}, model -> true, PlayerModel::getPlaytime, 0);
    }

    public void setStaffchatEnabled(@NotNull MinetopiaPlayer player, boolean staffchatEnabled) {
        StormDatabase.getInstance().updateModel(player, PlayerModel.class, playerModel -> playerModel.setStaffchatEnabled(staffchatEnabled));
    }
}