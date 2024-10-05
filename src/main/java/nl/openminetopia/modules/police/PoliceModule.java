package nl.openminetopia.modules.police;

import lombok.Getter;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.police.commands.EmergencyCommand;
import nl.openminetopia.modules.police.balaclava.listeners.PlayerArmorChangeListener;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PoliceModule extends Module {

    public HashMap<UUID, Long> emergencyCooldowns = new HashMap<>();

    @Override
    public void enable() {
        registerCommand(new EmergencyCommand());
        registerListener(new PlayerArmorChangeListener());
    }

    @Override
    public void disable() {

    }

}
