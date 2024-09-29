package nl.openminetopia.modules.banking.menu;

import com.jazzkuh.inventorylib.objects.PaginatedMenu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class BankAccountSelectionMenu extends PaginatedMenu {

    public BankAccountSelectionMenu(Player player, AccountType type) {
        super(ChatUtils.color(""), 4, InventoryType.CHEST, true);
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        Collection<BankAccountModel> accountModels = bankingModule.getAccountsFromPlayer(player.getUniqueId())
                .stream().filter(account -> account.getType() == type)
                .toList();

        for (BankAccountModel accountModel : accountModels) {
            ItemStack accountStack = new ItemBuilder(type.getMaterial())
                    .setName(type.getColor() + accountModel.getName())
                    .toItemStack();

            this.addItem(new Icon(accountStack, false, (event) -> {

            }));
        }
    }

    @Override
    public Icon getPreviousPageItem() {
        ItemStack previousStack = new ItemBuilder(Material.ARROW)
                .setName("Vorige Pagina")
                .toItemStack();
        return new Icon(30, previousStack, e -> e.setCancelled(true));
    }

    @Override
    public Icon getNextPageItem() {
        ItemStack previousStack = new ItemBuilder(Material.ARROW)
                .setName("Volgende Pagina")
                .toItemStack();
        return new Icon(32, previousStack, e -> e.setCancelled(true));
    }
}
