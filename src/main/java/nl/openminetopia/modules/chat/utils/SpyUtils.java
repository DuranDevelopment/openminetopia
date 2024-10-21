package nl.openminetopia.modules.chat.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class SpyUtils {

    public void chatSpy(Player player, String message, List<Player> ignore) {
        // TODO: Replace <player_name> <message> with actual values
        Component spiedMessage = MessageConfiguration.component("chat_chatspy_format");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) continue;
            if (ignore.contains(onlinePlayer)) continue;

            Optional<OnlineMinetopiaPlayer> optional = obtainPlayer(onlinePlayer);
            if (optional.isEmpty()) continue;

            OnlineMinetopiaPlayer mPlayer = optional.get();
            if (mPlayer.isChatSpyEnabled()) onlinePlayer.sendMessage(spiedMessage);
        }
    }

    public void commandSpy(Player player, String command) {
        // TODO: Replace <player_name> <command> with actual values
        Component spiedMessage = MessageConfiguration.component("chat_commandspy_format");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) continue;

            Optional<OnlineMinetopiaPlayer> optional = obtainPlayer(onlinePlayer);
            if (optional.isEmpty()) continue;

            OnlineMinetopiaPlayer mPlayer = optional.get();
            if (mPlayer.isCommandSpyEnabled()) onlinePlayer.sendMessage(spiedMessage);
        }
    }

    public Optional<OnlineMinetopiaPlayer> obtainPlayer(Player player) {
        OnlineMinetopiaPlayer mPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(player);
        return Optional.ofNullable(mPlayer);
    }

}
