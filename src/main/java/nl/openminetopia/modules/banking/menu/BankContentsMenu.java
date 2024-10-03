package nl.openminetopia.modules.banking.menu;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.PersistentDataUtil;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

@Getter
public class BankContentsMenu extends Menu {

    private final Player player;
    private final BankAccountModel accountModel;

    public BankContentsMenu(Player player, BankAccountModel accountModel) {
        super(ChatUtils.color(accountModel.getType().getColor() + accountModel.getName() + "<reset> | " + accountModel.getBalance()), 6);
        this.player = player;
        this.accountModel = accountModel;

        List<BankNote> bankNotes = new LinkedList<>();
        bankNotes.add(new BankNote(Material.GHAST_TEAR, 500));
        bankNotes.add(new BankNote(Material.DIAMOND, 200));
        bankNotes.add(new BankNote(Material.REDSTONE, 100));
        bankNotes.add(new BankNote(Material.EMERALD, 50));
        bankNotes.add(new BankNote(Material.COAL, 20));
        bankNotes.add(new BankNote(Material.IRON_INGOT, 10));
        bankNotes.add(new BankNote(Material.QUARTZ, 5));
        bankNotes.add(new BankNote(Material.GOLD_INGOT, 1));
        bankNotes.add(new BankNote(Material.GOLD_NUGGET, 0.10));

        for (int i = 36; i < 45; i++) {
            this.addItem(new Icon(i, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).toItemStack()));
        }

        int i = 45;
        for (BankNote bankNote : bankNotes) {
            this.addItem(new Icon(i, bankNote.toMenuItem(1), true, event -> withdrawMoney(bankNote, 1)));
            i++;
        }

        double remainingBalance = accountModel.getBalance();
        int slot = 0;

        for (BankNote bankNote : bankNotes) {
            int stackCount = (int) (remainingBalance / (bankNote.getValue() * 64));

            if (stackCount > 0) {
                for (int j = 0; j < stackCount; j++) {
                    this.addItem(new Icon(slot, bankNote.toMenuItem(64), true, event -> withdrawMoney(bankNote, 64)));
                    slot++;
                    if (slot >= 36) {
                        break;
                    }
                }

                remainingBalance -= stackCount * 64 * bankNote.getValue();
            }

            int remainingItems = (int) (remainingBalance / bankNote.getValue());
            if (remainingItems > 0 && slot < 36) {
                this.addItem(new Icon(slot, bankNote.toMenuItem(remainingItems), true, event -> withdrawMoney(bankNote, remainingItems)));
                remainingBalance -= remainingItems * bankNote.getValue();
                slot++;
            }

            if (slot >= 36) {
                break;
            }
        }

    }

    @Override
    @SuppressWarnings("all") // warning check for nothing
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        ItemStack item = event.getCurrentItem();

        if (!PersistentDataUtil.contains(item, "bank_note_value")) return;
        if (PersistentDataUtil.getDouble(item, "bank_note_value") == null) return;

        double noteValue = PersistentDataUtil.getDouble(item, "bank_note_value");
        double totalValue = noteValue * item.getAmount();

        item.setAmount(0);
        accountModel.setBalance(accountModel.getBalance() + totalValue);
        player.sendMessage(ChatUtils.color("<gold>Je hebt <red>" + totalValue + " <gold>gestort naar je rekening."));
        new BankContentsMenu(player, accountModel).open(player);
    }

    private void withdrawMoney(BankNote note, int amount) {
        double balance = accountModel.getBalance();
        double totalValue = note.getValue() * amount;

        if (balance < totalValue) {
            player.sendMessage(ChatUtils.color("<red>Je hebt niet genoeg op je rekening staan!"));
            return;
        }

        accountModel.setBalance(balance - totalValue);

        player.getInventory().addItem(note.toNote(amount));
        player.sendMessage(ChatUtils.color("<gold>Je hebt <red>" + (amount * note.getValue()) + " <gold>opgenomen van je rekening."));
        new BankContentsMenu(player, accountModel).open(player);
    }

    @Getter
    @RequiredArgsConstructor
    private final class BankNote {
        private final Material material;
        private final double value;

        private ItemStack toMenuItem(int amount) {
            return new ItemBuilder(material, amount)
                    .setName("<gold>€" + (amount * value))
                    .addLoreLine("<yellow>Klik om op te nemen.")
                    .toItemStack();
        }

        private ItemStack toNote(int amount) {
            return new ItemBuilder(material, amount)
                    .setName("<gold>€" + value)
                    .addLoreLine("<yellow>Officieel Horizons bankbiljet.")
                    .addLoreLine("<yellow>Eigendom van de Centrale Bank.")
                    .setNBT("bank_note_value", value)
                    .toItemStack();
        }

    }

}
