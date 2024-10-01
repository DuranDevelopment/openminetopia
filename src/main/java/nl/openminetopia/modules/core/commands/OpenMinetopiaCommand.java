package nl.openminetopia.modules.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.vehicles.VehiclesModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("openminetopia")
public class OpenMinetopiaCommand extends BaseCommand {

    @Subcommand("reload")
    @SneakyThrows
    @CommandPermission("openminetopia.reload")
    public void onReload(Player player) {
        OpenMinetopia.getDefaultConfiguration().getLoader().load();
        OpenMinetopia.setDefaultConfiguration(new DefaultConfiguration(OpenMinetopia.getInstance().getDataFolder()));
        OpenMinetopia.getModuleManager().getModule(VehiclesModule.class).loadConfiguration();
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
