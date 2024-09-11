package nl.openminetopia.api.player;

import lombok.Getter;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

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
    public HashMap<UUID, OnlineMinetopiaPlayer> minetopiaPlayers = new HashMap<>();


    public @Nullable OnlineMinetopiaPlayer getMinetopiaPlayer(@NotNull Player player) {
        if (!minetopiaPlayers.containsKey(player.getUniqueId())) {
            OnlineMinetopiaPlayer onlineMinetopiaPlayer = new OnlineMinetopiaPlayer(player.getUniqueId(), playerModels.get(player.getUniqueId()));
            minetopiaPlayers.put(player.getUniqueId(), onlineMinetopiaPlayer);
            return onlineMinetopiaPlayer;
        }

        return minetopiaPlayers.get(player.getUniqueId());
    }
}