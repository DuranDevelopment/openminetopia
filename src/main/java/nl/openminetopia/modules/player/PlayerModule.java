package nl.openminetopia.modules.player;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.player.listeners.PlayerJoinListener;
import nl.openminetopia.modules.player.listeners.PlayerPreLoginListener;
import nl.openminetopia.modules.player.listeners.PlayerQuitListener;

public class PlayerModule extends Module {

    @Override
    public void enable() {
        registerListener(new PlayerPreLoginListener());
        registerListener(new PlayerJoinListener());
        registerListener(new PlayerQuitListener());
    }

    @Override
    public void disable() {

    }
}
