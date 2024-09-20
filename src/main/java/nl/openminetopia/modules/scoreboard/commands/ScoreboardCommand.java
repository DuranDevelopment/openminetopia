package nl.openminetopia.modules.scoreboard.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import nl.openminetopia.api.player.ScoreboardManager;
import org.bukkit.entity.Player;

@CommandAlias("sb")
public class ScoreboardCommand extends BaseCommand {

    @Default
    public void onCommand(Player player) {
        if (ScoreboardManager.getInstance().getScoreboards().containsKey(player.getUniqueId())) {
            ScoreboardManager.getInstance().removeScoreboard(player);
            return;
        }
        ScoreboardManager.getInstance().addScoreboard(player);
    }
}
