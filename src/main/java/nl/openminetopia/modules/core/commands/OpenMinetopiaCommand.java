package nl.openminetopia.modules.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.ColorsConfiguration;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.configuration.LevelCheckConfiguration;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("openminetopia|sdb|minetopia|omt")
public class OpenMinetopiaCommand extends BaseCommand {

    @Subcommand("reload")
    @SneakyThrows
    @CommandPermission("openminetopia.reload")
    public void onReload(Player player) {
        OpenMinetopia.setDefaultConfiguration(new DefaultConfiguration(OpenMinetopia.getInstance().getDataFolder()));
        OpenMinetopia.getDefaultConfiguration().saveConfiguration();

        OpenMinetopia.setMessageConfiguration(new MessageConfiguration(OpenMinetopia.getInstance().getDataFolder()));
        OpenMinetopia.getMessageConfiguration().saveConfiguration();

        OpenMinetopia.setLevelcheckConfiguration(new LevelCheckConfiguration(OpenMinetopia.getInstance().getDataFolder()));
        OpenMinetopia.getLevelcheckConfiguration().saveConfiguration();

        OpenMinetopia.setColorsConfiguration(new ColorsConfiguration(OpenMinetopia.getInstance().getDataFolder()));
        OpenMinetopia.getColorsConfiguration().saveConfiguration();

        player.sendMessage(ChatUtils.color("<gold>De configuratiebestanden zijn succesvol herladen!"));
    }

    @Default
    public void onCommand(Player player) {
        player.sendMessage(ChatUtils.color(" "));
        player.sendMessage(ChatUtils.color("<gold>Deze server maakt gebruik van <yellow>OpenMinetopia <gold>versie <yellow>" + OpenMinetopia.getInstance().getDescription().getVersion()));
        player.sendMessage(ChatUtils.color("<gold>Auteurs: <yellow>" + OpenMinetopia.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        player.sendMessage(ChatUtils.color(" "));
    }
}
