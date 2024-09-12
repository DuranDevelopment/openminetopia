package nl.openminetopia.modules.scoreboard;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.scoreboard.listeners.PlayerJoinListener;
import nl.openminetopia.modules.scoreboard.listeners.PlayerQuitListener;

public class ScoreboardModule extends Module {

    @Override
    public void enable() {
        registerListener(new PlayerJoinListener());
        registerListener(new PlayerQuitListener());
    }

    @Override
    public void disable() {

    }
}
