package nl.openminetopia.modules.misc;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.misc.commands.HeadCommand;

public class MiscModule extends Module {

    @Override
    public void enable() {
        registerCommand(new HeadCommand());
    }

    @Override
    public void disable() {

    }
}
