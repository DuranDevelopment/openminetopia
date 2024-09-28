package nl.openminetopia.modules.core;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.core.commands.OpenMinetopiaCommand;

public class CoreModule extends Module {
    @Override
    public void enable() {
        registerCommand(new OpenMinetopiaCommand());
    }

    @Override
    public void disable() {

    }
}
