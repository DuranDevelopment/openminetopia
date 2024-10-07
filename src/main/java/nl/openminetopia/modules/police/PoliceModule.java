package nl.openminetopia.modules.police;

import lombok.Getter;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.police.commands.EmergencyCommand;
import nl.openminetopia.modules.police.balaclava.listeners.PlayerArmorChangeListener;
import nl.openminetopia.modules.police.handcuff.HandcuffManager;
import nl.openminetopia.modules.police.handcuff.listeners.*;
import nl.openminetopia.modules.police.handcuff.objects.HandcuffedPlayer;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PoliceModule extends Module {

    public HashMap<UUID, Long> emergencyCooldowns = new HashMap<>();

    @Override
    public void enable() {
        registerCommand(new EmergencyCommand());
        registerListener(new PlayerArmorChangeListener());

        registerListener(new PlayerInteractEntityListener());
        registerListener(new PlayerMoveListener());
        registerListener(new PlayerDropItemListener());
        registerListener(new PlayerEntityDamageListener());
        registerListener(new PlayerOpenInventoryListener());
        registerListener(new PlayerPickupItemListener());
        registerListener(new PlayerSlotChangeListener());
        registerListener(new PlayerInventoryClickListener());
    }

    @Override
    public void disable() {
        HandcuffManager.getInstance().getHandcuffedPlayers().forEach(HandcuffedPlayer::release);
    }
}
