package nl.openminetopia.modules.places.commands.mtworld.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.api.places.MTWorldManager;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("mtwereld|mtworld")
public class MTWorldSettingCommand extends BaseCommand {

    @Subcommand("setcolor")
    public void onSetColor(Player player, String worldName, String color) {
        // Remove the world from the database
        MTWorld mtWorld = MTWorldManager.getInstance().getWorld(worldName);
        if (mtWorld == null) {
            player.sendMessage(ChatUtils.color("<red>World <white>" + worldName + " <red>does not exist!"));
            return;
        }

        MTWorldManager.getInstance().setColor(mtWorld, color);
        player.sendMessage(ChatUtils.color("<red>World color of <" + color +  ">" + worldName + " <red>has been changed!"));
    }

    @Subcommand("settemperature")
    public void onSetTemperature(Player player, String worldName, Double temperatuur) {
        // Remove the world from the database
        MTWorld mtWorld = MTWorldManager.getInstance().getWorld(worldName);
        if (mtWorld == null) {
            player.sendMessage(ChatUtils.color("<red>World <white>" + worldName + " <red>does not exist!"));
            return;
        }

        MTWorldManager.getInstance().setTemperature(mtWorld, temperatuur);
        player.sendMessage(ChatUtils.color("<red>World temperatuur of <" + temperatuur +  ">" + worldName + " <red>has been changed!"));
    }

    @Subcommand("setloadingname")
    public void onSetLoadingname(Player player, String worldName, String loadingName) {
        // Remove the world from the database
        MTWorld mtWorld = MTWorldManager.getInstance().getWorld(worldName);
        if (mtWorld == null) {
            player.sendMessage(ChatUtils.color("<red>World <white>" + worldName + " <red>does not exist!"));
            return;
        }

        MTWorldManager.getInstance().setLoadingName(mtWorld, loadingName);
        player.sendMessage(ChatUtils.color("<red>World loadingName of <" + loadingName +  ">" + worldName + " <red>has been changed!"));
    }

    @Subcommand("settitle")
    public void onSetTitle(Player player, String worldName, String title) {
        // Remove the world from the database
        MTWorld mtWorld = MTWorldManager.getInstance().getWorld(worldName);
        if (mtWorld == null) {
            player.sendMessage(ChatUtils.color("<red>World <white>" + worldName + " <red>does not exist!"));
            return;
        }

        MTWorldManager.getInstance().setTitle(mtWorld, title);
        player.sendMessage(ChatUtils.color("<red>World title of <" + title +  ">" + worldName + " <red>has been changed!"));
    }
}
