package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
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

        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness info of <aqua>" + player.getName() + "<dark_aqua>:"));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Drinking points: <aqua>" + minetopiaPlayer.getDrinkingPoints()));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by drinking: <aqua>" + minetopiaPlayer.getFitnessGainedByDrinking()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by walking: <aqua>" + minetopiaPlayer.getFitnessGainedByWalking()));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by climbing: <aqua>" + minetopiaPlayer.getFitnessGainedByClimbing()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Total fitness: <aqua>" + minetopiaPlayer.getFitness()));
    }
}
