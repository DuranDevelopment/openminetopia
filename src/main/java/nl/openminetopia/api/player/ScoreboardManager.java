package nl.openminetopia.api.player;

import lombok.Getter;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.ChatUtils;
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

    public void updateBoard(MinetopiaPlayer minetopiaPlayer, Sidebar sidebar) {
        List<String> lines = configuration.getScoreboardLines();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i)
                    .replace("<fitness>", String.valueOf(minetopiaPlayer.getTotalPoints()))
                    .replace("<level>", String.valueOf(minetopiaPlayer.getLevel()))
                    .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix());
            if (i == 0) {
                sidebar.title(ChatUtils.color(line));
                continue;
            }
            sidebar.line(i, ChatUtils.color(line));
        }
    }

    public void addScoreboard(Player player, Sidebar sidebar) {
        sidebar.addPlayer(player);
        scoreboards.put(player.getUniqueId(), sidebar);
    }

    public void removeScoreboard(Player player) {
        getScoreboard(player.getUniqueId()).removePlayer(player);
        scoreboards.remove(player.getUniqueId());
    }

    public Sidebar getScoreboard(UUID uuid) {
        return scoreboards.get(uuid);
    }
}
