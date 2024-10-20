package nl.openminetopia.modules.fitness.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.MessageConfiguration;
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
            player.sendMessage(MessageConfiguration.component("player_not_found"));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer);
        if (minetopiaPlayer == null) {
            player.sendMessage(MessageConfiguration.component("database_read_error"));
            return;
        }

        // TODO: Replace <playername> with actual player name.
        player.sendMessage(MessageConfiguration.component("fitness_info_header"));
        player.sendMessage("");

        // TODO: Replace <amount> and <expires_at> with actual booster values.
        minetopiaPlayer.getFitness().getBoosters().forEach(fitnessBooster ->
                        player.sendMessage(MessageConfiguration.component("fitness_booster_info"))
        );
        player.sendMessage("");

        DrinkingStatistic drinkingStatistic = (DrinkingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.DRINKING);
        // TODO: Replace <points> with actual drinking points value.
        player.sendMessage(MessageConfiguration.component("fitness_drinking_points"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.

        player.sendMessage(MessageConfiguration.component("fitness_drinking_fitness"));
        player.sendMessage("");

        HealthStatistic healthStatistic = (HealthStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.HEALTH);
        // TODO: Replace <points> with actual health points value.
        player.sendMessage(MessageConfiguration.component("fitness_health_points"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_health_fitness"));
        player.sendMessage("");

        WalkingStatistic walkingStatistic = (WalkingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.WALKING);
        // TODO: Replace <distance> with actual distance walked (converted from cm to km).
        player.sendMessage(MessageConfiguration.component("fitness_walking_distance"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_walking_fitness"));
        player.sendMessage("");

        ClimbingStatistic climbingStatistic = (ClimbingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.CLIMBING);
        // TODO: Replace <distance> with actual distance climbed (converted from cm to km).
        player.sendMessage(MessageConfiguration.component("fitness_climbing_distance"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_climbing_fitness"));
        player.sendMessage("");

        SprintingStatistic sprintingStatistic = (SprintingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.SPRINTING);
        // TODO: Replace <distance> with actual distance sprinted (converted from cm to km).
        player.sendMessage(MessageConfiguration.component("fitness_sprinting_distance"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_sprinting_fitness"));
        player.sendMessage("");

        SwimmingStatistic swimmingStatistic = (SwimmingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.SWIMMING);
        // TODO: Replace <distance> with actual distance swam (converted from cm to km).
        player.sendMessage(MessageConfiguration.component("fitness_swimming_distance"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_swimming_fitness"));
        player.sendMessage("");

        FlyingStatistic flyingStatistic = (FlyingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.FLYING);
        // TODO: Replace <distance> with actual distance flown (converted from cm to km).
        player.sendMessage(MessageConfiguration.component("fitness_flying_distance"));
        // TODO: Replace <fitness> and <max_fitness> with actual fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_flying_fitness"));
        player.sendMessage("");

        TotalStatistic totalStatistic = (TotalStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL);
        // TODO: Replace <fitness> and <max_fitness> with actual total fitness gained and max fitness gainable.
        player.sendMessage(MessageConfiguration.component("fitness_total_fitness"));
    }
}
