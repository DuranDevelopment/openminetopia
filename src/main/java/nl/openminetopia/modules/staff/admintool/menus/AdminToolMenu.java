package nl.openminetopia.modules.staff.admintool.menus;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
public class AdminToolMenu extends Menu {

    private final OfflinePlayer offlinePlayer;
    private final Player player;

    public AdminToolMenu(Player player, OfflinePlayer offlinePlayer) {
        super(ChatUtils.color("<gold>Beheerscherm <yellow>" + offlinePlayer.getPlayerProfile().getName()), 3);
        this.offlinePlayer = offlinePlayer;
        this.player = player;

        ItemBuilder skullBuilder = new ItemBuilder(Material.PLAYER_HEAD)
                .setName("<gold>Minetopia Informatie")
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <yellow>MT-Info <gold>te openen.")
                .addLoreLine("")
                .setSkullOwner(offlinePlayer);

        Icon targetSkullIcon = new Icon(10, skullBuilder.toItemStack(), event -> {
            AdminToolInfoMenu menu = new AdminToolInfoMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());
        });
        this.addItem(targetSkullIcon);

        ItemBuilder enderchestBuilder = new ItemBuilder(Material.ENDER_CHEST)
                .setName("<gold>Enderchest")
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <yellow>enderchest <gold>te openen.")
                .addLoreLine("");

        Icon targetEnderchestIcon = new Icon(16, enderchestBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            player.sendMessage(ChatUtils.color("<dark_green>Je opent de enderchest van <green>" + targetPlayer.getName() + "<dark_green>."));
            player.openInventory(targetPlayer.getEnderChest());
        });

        if (offlinePlayer.isOnline()) this.addItem(targetEnderchestIcon);

        this.open(player);
    }
}
