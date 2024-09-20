package nl.openminetopia.api.player;

import lombok.Getter;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.scoreboard.ScoreboardModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class ScoreboardManager {

    private static ScoreboardManager instance;

    public static ScoreboardManager getInstance() {
        if (instance == null) {
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public HashMap<UUID, Sidebar> scoreboards = new HashMap<>();
    public DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

    public void updateBoard(MinetopiaPlayer minetopiaPlayer) {
        Sidebar sidebar = getScoreboard(minetopiaPlayer.getUuid());
        if (sidebar == null) return;

        List<String> lines = configuration.getScoreboardLines();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (i == 0) {
                sidebar.title(ChatUtils.format(minetopiaPlayer, line));
                continue;
            }
            sidebar.line(i, ChatUtils.format(minetopiaPlayer, line));
        }
    }

    public void addScoreboard(Player player) {
        Sidebar sidebar = OpenMinetopia.getModuleManager().getModule(ScoreboardModule.class).getScoreboardLibrary().createSidebar();
        sidebar.addPlayer(player);
        scoreboards.put(player.getUniqueId(), sidebar);
    }

    public void removeScoreboard(Player player) {
        Sidebar sidebar = getScoreboard(player.getUniqueId());
        sidebar.removePlayer(player);
        sidebar.close();
        scoreboards.remove(player.getUniqueId());
    }

    public Sidebar getScoreboard(UUID uuid) {
        return scoreboards.get(uuid);
    }
}
