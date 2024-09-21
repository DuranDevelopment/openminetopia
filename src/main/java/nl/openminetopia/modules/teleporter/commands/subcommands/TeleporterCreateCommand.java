package nl.openminetopia.modules.teleporter.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.modules.teleporter.utils.TeleporterUtil;
import nl.openminetopia.modules.teleporter.utils.enums.PressurePlate;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("teleporter")
public final class TeleporterCreateCommand extends BaseCommand {

    @Subcommand("create")
    @Syntax("[plate] [addDisplay] [roundYaw]")
    public void create(Player player, @Optional PressurePlate plate, @Optional Boolean addDisplay, @Optional Boolean roundYaw) {
        if (plate == null) plate = PressurePlate.STONE_PRESSURE_PLATE;
        if (addDisplay == null) addDisplay = true;
        if (roundYaw == null) roundYaw = false;

        Location location = player.getLocation();
        if (roundYaw) location.setYaw(Math.round(location.getYaw() / 45) * 45);

        ItemStack item = TeleporterUtil.buildPlate(plate, location, addDisplay);
        player.getInventory().addItem(item);

        player.sendMessage(ChatUtils.color("<gold>Teleporter successfully created."));
    }

}
