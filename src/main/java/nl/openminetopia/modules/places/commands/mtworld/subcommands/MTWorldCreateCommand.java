package nl.openminetopia.modules.places.commands.mtworld.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.api.places.MTWorldManager;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("mtwereld|mtworld")
public class MTWorldCreateCommand extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("openminetopia.world.create")
    public void onCreate(Player player, String loadingName) {
        String title = "<bold>" + loadingName.toUpperCase();

        for (MTWorld mtWorld : MTWorldManager.getInstance().getWorlds()) {
            if (mtWorld.getName().equalsIgnoreCase(loadingName)) {
                player.sendMessage(ChatUtils.color("<red>World <white>" + loadingName + " <red>already exists!"));
                return;
            }
        }

        MTWorld world = new MTWorld(player.getWorld().getName(), title, "<gold>", 21.64, loadingName);
        MTWorldManager.getInstance().createWorld(world);
        player.sendMessage(ChatUtils.color("<green>World <white>" + loadingName + " <green>has been created!"));
    }
}
