package nl.openminetopia.modules.config;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.config.commands.ConfigReloadCommand;

public class ConfigModule extends Module {
    @Override
    public void enable() {
        registerCommand(new ConfigReloadCommand());
    }

    @Override
    public void disable() {

    }
}
