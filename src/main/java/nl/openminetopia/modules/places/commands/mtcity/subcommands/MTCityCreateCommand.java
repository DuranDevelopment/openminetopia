package nl.openminetopia.modules.places.commands.mtcity.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.places.MTCityManager;
import nl.openminetopia.api.places.objects.MTCity;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.entity.Player;

@CommandAlias("mtstad|mtcity")
public class MTCityCreateCommand extends BaseCommand {

    @Subcommand("create")
    public void onCreate(Player player, String name, String loadingName) {

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        MTWorld world = minetopiaPlayer.getWorld();
        if (world == null) {
            player.sendMessage(ChatUtils.color("<red>You are not in a world!"));
            return;
        }

        for (MTCity city : MTCityManager.getInstance().getCities()) {
            if (city.getName().equalsIgnoreCase(name)) {
                player.sendMessage(ChatUtils.color("<red>City <white>" + name + " <red>already exists!"));
                return;
            }
        }

        // check if region exists
        for (ProtectedRegion region : WorldGuardUtils.getProtectedRegions(player.getWorld(), priority -> priority >= 0)) {
            if (!region.getId().equalsIgnoreCase(name)) continue;

            String title = "<bold>" + loadingName.toUpperCase();
            MTCity city = new MTCity(world.getId(), name, title, "<gold>", 21.64, loadingName);
            MTCityManager.getInstance().createCity(city);

            player.sendMessage(ChatUtils.color("<green>City <white>" + loadingName + " <green>has been created!"));
            return;
        }

        player.sendMessage(ChatUtils.color("<red>Region <white>" + name + " <red>does not exist!"));
    }
}
