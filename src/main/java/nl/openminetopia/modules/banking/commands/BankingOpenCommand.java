package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.jazzkuh.inventorylib.objects.Menu;
import com.sk89q.minecraft.util.commands.Command;
import net.kyori.adventure.text.Component;
import nl.openminetopia.modules.banking.menu.BankTypeSelectionMenu;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("accounts|account|rekening")
public class BankingOpenCommand extends BaseCommand {

    @Default
    public void openBank(Player player) {
        new BankTypeSelectionMenu(player).open(player);
    }

}
