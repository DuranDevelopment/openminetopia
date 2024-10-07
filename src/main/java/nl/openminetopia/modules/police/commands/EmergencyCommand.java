package nl.openminetopia.modules.police.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.police.PoliceModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("112|911")
public class EmergencyCommand extends BaseCommand {

    @Default
    public void emergency(Player player, String message) {
        if (hasCooldown(player)) {
            long cooldown = OpenMinetopia.getModuleManager().getModule(PoliceModule.class).getEmergencyCooldowns().get(player.getUniqueId());
            player.sendMessage(ChatUtils.color("<red><b>112</b></red> <dark_gray>| <gray>Je kan niet zo snel achter elkaar een melding maken."));
            player.sendMessage(ChatUtils.color("<red><b>112</b></red> <dark_gray>| <gray>Probeer het over <white>" + cooldownToTime(cooldown) + "<gray> nog eens."));
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("openminetopia.police")) return;

            onlinePlayer.sendMessage(ChatUtils.color("<red><b>112</b></red> <dark_gray>| <gray>" + player.getName() + ": <white>" + message));
        }

        OpenMinetopia.getModuleManager().getModule(PoliceModule.class).getEmergencyCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
    }


    private boolean hasCooldown(Player player) {
        PoliceModule policeModule = OpenMinetopia.getModuleManager().getModule(PoliceModule.class);

        if (!policeModule.getEmergencyCooldowns().containsKey(player.getUniqueId())) return false;

        long cooldown = policeModule.getEmergencyCooldowns().get(player.getUniqueId());
        // check if cooldown of 5 minutes has passed and remove from map if so
        if (System.currentTimeMillis() - cooldown >= 300000) {
            policeModule.getEmergencyCooldowns().remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    private String cooldownToTime(long cooldown) {
        long millis = 300000 - (System.currentTimeMillis() - cooldown);
        long seconds = millisToSeconds(millis);
        long minutes = millisToMinutes(millis);
        return minutes + " minuten en " + seconds + " seconden";
    }

    private long millisToSeconds(long millis) {
        return millis / 1000;
    }

    private long millisToMinutes(long millis) {
        return millis / 60000;
    }
}
