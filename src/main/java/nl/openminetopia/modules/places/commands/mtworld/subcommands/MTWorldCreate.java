package nl.openminetopia.modules.places.commands.mtworld.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.api.places.MTWorldManager;
import nl.openminetopia.api.places.objects.MTWorld;
import org.bukkit.entity.Player;

@CommandAlias("mtworld")
public class MTWorldCreate extends BaseCommand {

    @Subcommand("create")
    public void onCreate(Player player, String loadingName) {
        String title = "<bold>" + loadingName.toUpperCase();
        MTWorld world = new MTWorld(player.getWorld().getName(), title, "<gold>", 21.64, loadingName);
        MTWorldManager.getInstance().createWorld(world);

        for (MTWorld mtWorld : MTWorldManager.getInstance().getWorlds()) {
            if (mtWorld.getName().equalsIgnoreCase(loadingName)) {
                player.sendMessage("<red>World <white>" + loadingName + " <red>already exists!");
                return;
            }
        }

        player.sendMessage("<green>World <white>" + loadingName + " <green>has been created!");
    }
}
