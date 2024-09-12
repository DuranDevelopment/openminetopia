package nl.openminetopia.api.player;

import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;

import java.util.HashMap;
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

    public HashMap<UUID, FastBoard> scoreboards = new HashMap<>();

    public void updateBoard(MinetopiaPlayer minetopiaPlayer, FastBoard board) {
        board.updateTitle(ChatUtils.color("<red>OpenMinetopia"));
        board.updateLines(
                ChatUtils.color("<gray>Fitheid: " + minetopiaPlayer.getTotalPoints()),
                ChatUtils.color("<gray>Level: " + minetopiaPlayer.getLevel()),
                ChatUtils.color("<gray>Prefix: " + minetopiaPlayer.getActivePrefix().getPrefix())
        );
    }

    public void addScoreboard(UUID uuid, FastBoard board) {
        scoreboards.put(uuid, board);
    }

    public void removeScoreboard(UUID uuid) {
        scoreboards.remove(uuid);
    }

    public FastBoard getScoreboard(UUID uuid) {
        return scoreboards.get(uuid);
    }
}
