package nl.openminetopia.modules.police.handcuff.runnables;

import net.kyori.adventure.title.Title;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.police.handcuff.objects.HandcuffedPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

public class HandcuffRunnable extends BukkitRunnable {

    private final HandcuffedPlayer handcuffedPlayer;

    public HandcuffRunnable(HandcuffedPlayer handcuffedPlayer) {
        this.handcuffedPlayer = handcuffedPlayer;
    }

    @Override
    public void run() {
        if (!handcuffedPlayer.getSource().isOnline()) {
            handcuffedPlayer.release();
        }

        Player source = handcuffedPlayer.getSource();
        Player player = handcuffedPlayer.getPlayer();

        if (OpenMinetopia.getDefaultConfiguration().isHandcuffShowTitle()) {
            Title title = Title.title(ChatUtils.color("<red>Je bent in de boeien geslagen!"),
                    ChatUtils.color("<red>Je kan niet bewegen!"),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1))
            );
            player.showTitle(title);
        }

        if (source.getLocation().distance(player.getLocation()) > 5) {
            player.teleport(source.getLocation());
        }
    }
}
