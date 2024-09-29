package nl.openminetopia.modules.chat;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.chat.listeners.PlayerChatListener;
import nl.openminetopia.modules.chat.listeners.PlayerCommandListener;

public class ChatModule extends Module {

    @Override
    public void enable() {
        registerListener(new PlayerChatListener());
        registerListener(new PlayerCommandListener());
    }

    @Override
    public void disable() {
        // Unregister listeners and commands
    }
}
