package nl.openminetopia.modules.player.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.concurrent.CompletableFuture;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        PlayerManager.getInstance().getPlayerModels().remove(event.getUniqueId());

        try {
            CompletableFuture<PlayerModel> future = StormDatabase.getInstance().loadPlayerModel(event.getUniqueId());
            future.whenComplete((playerModel, throwable) -> {
                if (throwable != null) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
                    OpenMinetopia.getInstance().getLogger().warning("Error loading player model: " + throwable.getMessage());
                    return;
                }

                if (playerModel == null) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
                    return;
                }

                PlayerManager.getInstance().getPlayerModels().put(event.getUniqueId(), playerModel);
            });
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
            OpenMinetopia.getInstance().getLogger().warning("Error loading player model: " + e.getMessage());
        }
    }
}
