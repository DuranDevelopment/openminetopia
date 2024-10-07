package nl.openminetopia.modules.places.commands.mtcity.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.api.places.MTCityManager;
import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("mtstad|mtcity")
public class MTCityRemoveCommand extends BaseCommand {

    @Subcommand("remove")
    @CommandPermission("openminetopia.city.remove")
    public void onRemove(Player player, String cityName) {
        // Remove the world from the database
        MTCity mtWorld = MTCityManager.getInstance().getCity(cityName);
        if (mtWorld == null) {
            player.sendMessage(ChatUtils.color("<red>World <white>" + cityName + " <red>does not exist!"));
            return;
        }

        MTCityManager.getInstance().deleteCity(mtWorld);
        player.sendMessage(ChatUtils.color("<red>World <white>" + cityName + " <red>has been removed!"));
    }
}
