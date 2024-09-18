package nl.openminetopia.modules.player.listeners;

import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.api.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        OnlineMinetopiaPlayer minetopiaPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        minetopiaPlayer.save();
//        PlayerModel playerModel = minetopiaPlayer.getPlayerModel();
//        StormDatabase.getInstance().saveStormModel(playerModel);

        minetopiaPlayer.getFitnessRunnable().cancel();
        minetopiaPlayer.getPlaytimeRunnable().cancel();

        PlayerManager.getInstance().getMinetopiaPlayers().remove(player.getUniqueId());
        PlayerManager.getInstance().getPlayerModels().remove(player.getUniqueId());
    }
}