package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.objects.FitnessBooster;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.MessageConfiguration;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("fitness")
public class FitnessBoosterCommand extends BaseCommand {

    @Subcommand("booster")
    @CommandPermission("openminetopia.fitness.booster")
    @CommandCompletion("@players")
    public void onBooster(Player player, OfflinePlayer offlinePlayer, int amount, @Optional Integer expiresAt) {
        if (offlinePlayer.getPlayer() == null) return;
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        if (expiresAt == null || expiresAt <= 0) expiresAt = -1;
        long expiresAtMillis = expiresAt == -1 ? -1 : System.currentTimeMillis() + (expiresAt * 1000);


        FitnessBooster fitnessBooster = new FitnessBooster(amount, expiresAtMillis);
        minetopiaPlayer.getFitness().addBooster(fitnessBooster);

        // TODO: Replace <playername> with actual value
        player.sendMessage(MessageConfiguration.component("fitness_booster_added_to"));
    }
}
