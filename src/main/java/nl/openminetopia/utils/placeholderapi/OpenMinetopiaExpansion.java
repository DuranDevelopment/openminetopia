package nl.openminetopia.utils.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class OpenMinetopiaExpansion extends PlaceholderExpansion {

    private final OpenMinetopia plugin = OpenMinetopia.getInstance();

    @Override
    public @NotNull String getIdentifier() {
        return "OpenMinetopia";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return null;

        if (params.equalsIgnoreCase("prefix")) {
            return minetopiaPlayer.getActivePrefix().getPrefix();
        }

        if (params.equalsIgnoreCase("level")) {
            return minetopiaPlayer.getLevel() + "";
        }

        if (params.equalsIgnoreCase("city")) {
            return minetopiaPlayer.getPlace().getName();
        }

        if (params.equalsIgnoreCase("world")) {
            return minetopiaPlayer.getWorld().getName();
        }

        if (params.equalsIgnoreCase("temperature")) {
            return minetopiaPlayer.getPlace().getTemperature() + "";
        }

        if(params.startsWith("balance")) {
            BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
            BankAccountModel accountModel = bankingModule.getAccountById(player.getUniqueId());
            if(accountModel == null) return null;

            if(params.equalsIgnoreCase("balance_formatted")) {
                return bankingModule.format(accountModel.getBalance());
            }

            return String.valueOf(accountModel.getBalance());
        }

        if (params.equalsIgnoreCase("fitness")) {
            return minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL).getFitnessGained() + "";
        }

        if (params.equalsIgnoreCase("max_fitness")) {
            return minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL).getMaxFitnessGainable() + "";
        }

        return null; //
    }
}
