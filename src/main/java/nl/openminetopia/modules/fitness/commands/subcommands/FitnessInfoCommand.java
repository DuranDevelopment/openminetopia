package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("fitness")
public class FitnessInfoCommand extends BaseCommand {

    @Subcommand("info")
    @Syntax("<player>")
    @CommandCompletion("@players")
    @Description("Get the fitness info of a player.")
    public void onInfoCommand(Player player, OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            player.sendMessage(ChatUtils.color("<red>This player does not exist."));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer);
        if (minetopiaPlayer == null) {
            player.sendMessage(ChatUtils.color("<red>There was an error loading the data of this player! Please try again later."));
            return;
        }

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness info of <aqua>" + offlinePlayer.getName() + "<dark_aqua>:"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Drinking points: <aqua>" + minetopiaPlayer.getDrinkingPoints()));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by drinking: <aqua>" + minetopiaPlayer.getFitnessGainedByDrinking() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByDrinking()));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers walked: <aqua>" + (offlinePlayer.getStatistic(Statistic.WALK_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by walking: <aqua>" + minetopiaPlayer.getFitnessGainedByWalking() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByWalking()));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers climbed: <aqua>" + (offlinePlayer.getStatistic(Statistic.CLIMB_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by climbing: <aqua>" + minetopiaPlayer.getFitnessGainedByClimbing() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByClimbing()));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers sprinted: <aqua>" + (offlinePlayer.getStatistic(Statistic.SPRINT_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by sprinting: <aqua>" + minetopiaPlayer.getFitnessGainedBySprinting() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessBySprinting()));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers swam: <aqua>" + (offlinePlayer.getStatistic(Statistic.SWIM_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by swimming: <aqua>" + minetopiaPlayer.getFitnessGainedBySwimming() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessBySwimming()));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers flown: <aqua>" + (offlinePlayer.getStatistic(Statistic.AVIATE_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by flying: <aqua>" + minetopiaPlayer.getFitnessGainedByFlying() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByFlying()));
        player.sendMessage("");
        player.sendMessage(ChatUtils.color("<dark_aqua>Total fitness: <aqua>" + minetopiaPlayer.getFitness() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessLevel()));
    }
}
