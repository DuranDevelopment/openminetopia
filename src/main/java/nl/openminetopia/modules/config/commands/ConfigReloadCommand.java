package nl.openminetopia.modules.config.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("openminetopia")
public class ConfigReloadCommand extends BaseCommand {

    @Subcommand("reload")
    @SneakyThrows
    public void onReload(Player player) {
        OpenMinetopia.getDefaultConfiguration().getLoader().load();
        OpenMinetopia.setDefaultConfiguration(new DefaultConfiguration(OpenMinetopia.getInstance().getDataFolder()));
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
