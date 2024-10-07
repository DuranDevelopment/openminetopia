package nl.openminetopia.modules.police.handcuff.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.police.handcuff.runnables.HandcuffRunnable;
import nl.openminetopia.modules.police.handcuff.utils.HandcuffUtils;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Data
public class HandcuffedPlayer {

    private final Player source;
    private final Player player;
    private final HandcuffRunnable handcuffRunnable = new HandcuffRunnable(this);

    public void handcuff() {
        HandcuffUtils.applyHandcuffEffects(player);
        player.sendMessage(ChatUtils.color("<red>Je bent in de boeien geslagen!"));
        handcuffRunnable.runTaskTimer(OpenMinetopia.getInstance(), 0, 20);
    }

    public void release() {
        HandcuffUtils.clearHandcuffEffects(player);
        player.sendMessage(ChatUtils.color("<red>Je bent uit de boeien gehaald!"));
        handcuffRunnable.cancel();
    }
}
