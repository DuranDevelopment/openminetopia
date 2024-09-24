package nl.openminetopia.modules.places.commands.mtcity.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.api.places.MTCityManager;
import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("mtstad|mtcity")
public class MTCitySettingCommand extends BaseCommand {

    @Subcommand("setcolor")
    public void onSetColor(Player player, String cityName, String color) {
        // Remove the city from the database
        MTCity MTCity = MTCityManager.getInstance().getCity(cityName);
        if (MTCity == null) {
            player.sendMessage(ChatUtils.color("<red>City <white>" + cityName + " <red>does not exist!"));
            return;
        }

        MTCityManager.getInstance().setColor(MTCity, color);
        player.sendMessage(ChatUtils.color("<red>City color of <" + color +  ">" + cityName + " <red>has been changed!"));
    }

    @Subcommand("settemperature")
    public void onSetTemperature(Player player, String cityName, Double temperatuur) {
        // Remove the city from the database
        MTCity MTCity = MTCityManager.getInstance().getCity(cityName);
        if (MTCity == null) {
            player.sendMessage(ChatUtils.color("<red>City <white>" + cityName + " <red>does not exist!"));
            return;
        }

        MTCityManager.getInstance().setTemperature(MTCity, temperatuur);
        player.sendMessage(ChatUtils.color("<red>City temperatuur of <" + temperatuur +  ">" + cityName + " <red>has been changed!"));
    }

    @Subcommand("setloadingname")
    public void onSetLoadingname(Player player, String cityName, String loadingName) {
        // Remove the city from the database
        MTCity MTCity = MTCityManager.getInstance().getCity(cityName);
        if (MTCity == null) {
            player.sendMessage(ChatUtils.color("<red>City <white>" + cityName + " <red>does not exist!"));
            return;
        }

        MTCityManager.getInstance().setLoadingName(MTCity, loadingName);
        player.sendMessage(ChatUtils.color("<red>City loadingName of <" + loadingName +  ">" + cityName + " <red>has been changed!"));
    }

    @Subcommand("settitle")
    public void onSetTitle(Player player, String cityName, String title) {
        // Remove the city from the database
        MTCity MTCity = MTCityManager.getInstance().getCity(cityName);
        if (MTCity == null) {
            player.sendMessage(ChatUtils.color("<red>City <white>" + cityName + " <red>does not exist!"));
            return;
        }

        MTCityManager.getInstance().setTitle(MTCity, title);
        player.sendMessage(ChatUtils.color("<red>City title of <" + title +  ">" + cityName + " <red>has been changed!"));
    }
}
