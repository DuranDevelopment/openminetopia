package nl.openminetopia.modules.places.commands.mtworld.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.api.places.MTWorldManager;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("mtwereld|mtworld")
public class MTWorldRemoveCommand extends BaseCommand {

    @Subcommand("remove")
    public void onRemove(Player player, String worldName) {
        // Remove the world from the database
        MTWorld mtWorld = MTWorldManager.getInstance().getWorld(worldName);
        if (mtWorld == null) {
            player.sendMessage(ChatUtils.color("<red>World <white>" + worldName + " <red>does not exist!"));
            return;
        }

        MTWorldManager.getInstance().deleteWorld(mtWorld);
        player.sendMessage(ChatUtils.color("<red>World <white>" + worldName + " <red>has been removed!"));
    }
}
