package nl.openminetopia.modules.banking.menu;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BankTypeSelectionMenu extends Menu {

    public BankTypeSelectionMenu(Player player) {
        super(ChatUtils.color("<gold>Selecteer het rekeningtype:"), 3);

        for(AccountType type : AccountType.values()) {
            ItemStack iconStack = new ItemBuilder(type.getMaterial())
                    .setName(type.getName())
                    .toItemStack();

            this.addItem(new Icon(type.getSlot(), iconStack, true, (event) -> {
                event.setCancelled(true);
                new BankAccountSelectionMenu(player, type).open(player);
            }));
        }

    }
}
