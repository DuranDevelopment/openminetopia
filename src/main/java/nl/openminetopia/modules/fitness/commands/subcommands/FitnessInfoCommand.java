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
    public void onInfoCommand(CommandSender sender, OfflinePlayer offlinePlayer) {
        Player player = offlinePlayer.getPlayer();
        if (player == null) return;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness info of <aqua>" + player.getName() + "<dark_aqua>:"));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Drinking points: <aqua>" + minetopiaPlayer.getDrinkingPoints()));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by drinking: <aqua>" + minetopiaPlayer.getFitnessGainedByDrinking() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByDrinking()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Kilometers walked: <aqua>" + (player.getStatistic(Statistic.WALK_ONE_CM) / 100000) + "km"));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by walking: <aqua>" + minetopiaPlayer.getFitnessGainedByWalking() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByWalking()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Kilometers climbed: <aqua>" + (player.getStatistic(Statistic.CLIMB_ONE_CM) / 100000) + "km"));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by climbing: <aqua>" + minetopiaPlayer.getFitnessGainedByClimbing() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByClimbing()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Kilometers sprinted: <aqua>" + (player.getStatistic(Statistic.SPRINT_ONE_CM) / 100000) + "km"));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by sprinting: <aqua>" + minetopiaPlayer.getFitnessGainedBySprinting() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessBySprinting()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Kilometers swam: <aqua>" + (player.getStatistic(Statistic.SWIM_ONE_CM) / 100000) + "km"));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by swimming: <aqua>" + minetopiaPlayer.getFitnessGainedBySwimming() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessBySwimming()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Kilometers flown: <aqua>" + (player.getStatistic(Statistic.AVIATE_ONE_CM) / 100000) + "km"));
        sender.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by flying: <aqua>" + minetopiaPlayer.getFitnessGainedByFlying() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessByFlying()));
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.color("<dark_aqua>Total fitness: <aqua>" + minetopiaPlayer.getFitness() + "<dark_aqua>/<aqua>" + configuration.getMaxFitnessLevel()));
    }
}
