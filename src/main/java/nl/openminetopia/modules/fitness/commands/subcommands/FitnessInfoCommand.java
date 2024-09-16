package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("fitness")
public class FitnessInfoCommand extends BaseCommand {

    @Subcommand("info")
    @Syntax("<player>")
    @CommandCompletion("@players")
    @Description("Get the fitness info of a player.")
    public void onInfoCommand(CommandSender sender, OfflinePlayer offlinePlayer) {
        Player player = offlinePlayer.getPlayer();
        if (player == null) return;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        sender.sendMessage("Fitness info of " + player.getName() + ":");
        sender.sendMessage("Walking points: " + minetopiaPlayer.getWalkingPoints());
        sender.sendMessage("Climbing points: " + minetopiaPlayer.getClimbingPoints());
        sender.sendMessage("Total points: " + minetopiaPlayer.getTotalPoints());

    }
}
