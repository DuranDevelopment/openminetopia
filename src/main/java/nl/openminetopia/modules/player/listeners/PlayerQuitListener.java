package nl.openminetopia.modules.player.listeners;

import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
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

        minetopiaPlayer.save().whenComplete((unused, throwable) -> {
            if (throwable != null) throwable.printStackTrace();
        });
//        PlayerModel playerModel = minetopiaPlayer.getPlayerModel();
//        StormDatabase.getInstance().saveStormModel(playerModel);

        minetopiaPlayer.getFitnessRunnable().cancel();
        minetopiaPlayer.getPlaytimeRunnable().cancel();

        PlayerManager.getInstance().getMinetopiaPlayers().remove(player.getUniqueId());
        PlayerManager.getInstance().getPlayerModels().remove(player.getUniqueId());
    }
}