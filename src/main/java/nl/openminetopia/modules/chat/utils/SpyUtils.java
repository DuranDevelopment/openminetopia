package nl.openminetopia.modules.chat.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class SpyUtils {

    public void chatSpy(Player player, String message, List<Player> ignore) {
        Component spiedMessage = ChatUtils
                .color("<dark_gray>[<gray>ChatSpy<dark_gray>]<gray> " + player.getName() + ": " + message);

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
        Component spiedMessage = ChatUtils
                .color("<dark_gray>[<gray>CommandSpy<dark_gray>]<gray> " + player.getName() + ": " + command);

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
