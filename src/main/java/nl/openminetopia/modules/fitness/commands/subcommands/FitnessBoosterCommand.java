package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessBoostersModel;
import nl.openminetopia.modules.fitness.objects.FitnessBooster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("fitness")
public class FitnessBoosterCommand extends BaseCommand {

    @Subcommand("booster")
    @Syntax("<player> <amount> [expiresAt]")
    @CommandCompletion("@players")
    public void onBooster(Player player, OfflinePlayer offlinePlayer, int amount, @Optional int expiresAt) {
        if (offlinePlayer.getPlayer() == null) return;
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        if (expiresAt == 0) expiresAt = -1;

        int nextId = StormDatabase.getInstance().getNextId(FitnessBoostersModel.class, FitnessBoostersModel::getId);
        FitnessBooster fitnessBooster = new FitnessBooster(nextId, amount, System.currentTimeMillis() + expiresAt);
        minetopiaPlayer.addFitnessBooster(fitnessBooster);

        if (minetopiaPlayer instanceof OnlineMinetopiaPlayer onlineMinetopiaPlayer) onlineMinetopiaPlayer.getFitnessRunnable().run();

        player.sendMessage("Added fitness booster to " + offlinePlayer.getName());
        minetopiaPlayer.getFitnessBoosters().forEach(fitnessBooster1 -> player.sendMessage("Booster: " + fitnessBooster1.getAmount() + " - " + fitnessBooster1.getExpiresAt()));
    }
}
