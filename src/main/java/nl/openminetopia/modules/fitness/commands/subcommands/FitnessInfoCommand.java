package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@CommandAlias("fitness")
public class FitnessInfoCommand extends BaseCommand {

    @Subcommand("info")
    @Syntax("<player>")
    @CommandCompletion("@players")
    @CommandPermission("openminetopia.fitness.info")
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

        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness info of <aqua>" + offlinePlayer.getName() + "<dark_aqua>:"));
        player.sendMessage("");

        minetopiaPlayer.getFitness().getBoosters().forEach(fitnessBooster -> player.sendMessage(ChatUtils.color("<dark_aqua>Booster: <aqua>" + fitnessBooster.getAmount() + " - " + fitnessBooster.getExpiresAt())));
        player.sendMessage("");

        DrinkingStatistic drinkingStatistic = (DrinkingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.DRINKING);
        player.sendMessage(ChatUtils.color("<dark_aqua>Drinking points: <aqua>" + drinkingStatistic.getPoints()));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by drinking: <aqua>" + drinkingStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + drinkingStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        HealthStatistic healthStatistic = (HealthStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.HEALTH);
        player.sendMessage(ChatUtils.color("<dark_aqua>Health points: <aqua>" + healthStatistic.getPoints()));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by health: <aqua>" + healthStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + healthStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        WalkingStatistic walkingStatistic = (WalkingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.WALKING);
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers walked: <aqua>" + (offlinePlayer.getStatistic(Statistic.WALK_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by walking: <aqua>" + walkingStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + walkingStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        ClimbingStatistic climbingStatistic = (ClimbingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.CLIMBING);
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers climbed: <aqua>" + (offlinePlayer.getStatistic(Statistic.CLIMB_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by climbing: <aqua>" + climbingStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + climbingStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        SprintingStatistic sprintingStatistic = (SprintingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.SPRINTING);
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers sprinted: <aqua>" + (offlinePlayer.getStatistic(Statistic.SPRINT_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by sprinting: <aqua>" + sprintingStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + sprintingStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        SwimmingStatistic swimmingStatistic = (SwimmingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.SWIMMING);
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers swam: <aqua>" + (offlinePlayer.getStatistic(Statistic.SWIM_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by swimming: <aqua>" + swimmingStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + swimmingStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        FlyingStatistic flyingStatistic = (FlyingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.FLYING);
        player.sendMessage(ChatUtils.color("<dark_aqua>Kilometers flown: <aqua>" + (offlinePlayer.getStatistic(Statistic.AVIATE_ONE_CM) / 100000) + "km"));
        player.sendMessage(ChatUtils.color("<dark_aqua>Fitness gained by flying: <aqua>" + flyingStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + flyingStatistic.getMaxFitnessGainable()));
        player.sendMessage("");
        TotalStatistic totalStatistic = (TotalStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL);
        player.sendMessage(ChatUtils.color("<dark_aqua>Total fitness: <aqua>" + totalStatistic.getFitnessGained() + "<dark_aqua>/<aqua>" + totalStatistic.getMaxFitnessGainable()));
    }
}
