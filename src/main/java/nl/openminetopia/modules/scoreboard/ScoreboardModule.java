package nl.openminetopia.modules.scoreboard;

import lombok.Getter;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.scoreboard.listeners.PlayerJoinListener;
import nl.openminetopia.modules.scoreboard.listeners.PlayerQuitListener;

@Getter
public class ScoreboardModule extends Module {

    private final DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();
    private ScoreboardLibrary scoreboardLibrary;

    @Override
    public void enable() {
        if (!configuration.isScoreboardEnabled()) return;

        registerListener(new PlayerJoinListener());
        registerListener(new PlayerQuitListener());

        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(OpenMinetopia.getInstance());
        } catch (NoPacketAdapterAvailableException e) {
            // If no packet adapter was found, you can fallback to the no-op implementation:
            scoreboardLibrary = new NoopScoreboardLibrary();
            OpenMinetopia.getInstance().getLogger().info("No scoreboard packet adapter available!");
        }

    }

    @Override
    public void disable() {
        scoreboardLibrary.close();
    }
}
