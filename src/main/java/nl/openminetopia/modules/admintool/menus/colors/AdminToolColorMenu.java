package nl.openminetopia.modules.admintool.menus.colors;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import nl.openminetopia.modules.admintool.menus.AdminToolInfoMenu;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
public class AdminToolColorMenu extends Menu {

    private final OfflinePlayer offlinePlayer;
    private final Player player;

    public AdminToolColorMenu(Player player, OfflinePlayer offlinePlayer) {
        super(ChatUtils.color("<gold>Beheerscherm <yellow>" + offlinePlayer.getPlayerProfile().getName()), 3);
        this.offlinePlayer = offlinePlayer;
        this.player = player;




        this.open(player);
    }
}
