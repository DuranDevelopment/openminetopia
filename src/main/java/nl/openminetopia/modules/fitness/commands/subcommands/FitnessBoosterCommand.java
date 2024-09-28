package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.models.FitnessBoosterModel;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.modules.data.utils.StormUtils;
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

        StormUtils.getNextId(FitnessBoosterModel.class, FitnessBoosterModel::getId).whenComplete((nextId, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            FitnessBooster fitnessBooster = new FitnessBooster(nextId, amount, expiresAtMillis);
            minetopiaPlayer.getFitness().addBooster(fitnessBooster);
        });

        if (minetopiaPlayer instanceof OnlineMinetopiaPlayer onlineMinetopiaPlayer) onlineMinetopiaPlayer.getFitnessRunnable().run();

        player.sendMessage("Added fitness booster to " + offlinePlayer.getName());
    }
}
