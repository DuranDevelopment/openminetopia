package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessBoosterModel;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("fitness")
public class FitnessBoosterCommand extends BaseCommand {

    @Subcommand("booster")
    @Syntax("<player> <amount> [expiresAt]")
    @CommandCompletion("@players")
    public void onBooster(Player player, OfflinePlayer offlinePlayer, int amount, @Optional Integer expiresAt) {
        if (offlinePlayer.getPlayer() == null) return;
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        if (expiresAt == null || expiresAt <= 0) expiresAt = -1;

        int nextId = StormDatabase.getInstance().getNextId(FitnessBoosterModel.class, FitnessBoosterModel::getId);
        FitnessBooster fitnessBooster = new FitnessBooster(nextId, amount, System.currentTimeMillis() + expiresAt);
        minetopiaPlayer.getFitness().addFitnessBooster(fitnessBooster);

        if (minetopiaPlayer instanceof OnlineMinetopiaPlayer onlineMinetopiaPlayer) onlineMinetopiaPlayer.getFitnessRunnable().run();

        player.sendMessage("Added fitness booster to " + offlinePlayer.getName());
        minetopiaPlayer.getFitness().getFitnessBoosters().forEach(fitnessBooster1 -> player.sendMessage("Booster: " + fitnessBooster1.getAmount() + " - " + fitnessBooster1.getExpiresAt()));
    }
}
