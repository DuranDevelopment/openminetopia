package nl.openminetopia.modules.staff.admintool.menus.colors;

import com.jazzkuh.inventorylib.objects.Menu;
import lombok.Getter;
import nl.openminetopia.utils.ChatUtils;
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
