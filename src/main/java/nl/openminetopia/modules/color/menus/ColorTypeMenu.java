package nl.openminetopia.modules.color.menus;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ColorTypeMenu extends Menu {
    public ColorTypeMenu(Player player, OfflinePlayer target) {
        super(ChatUtils.color("<black>Kies een kleurtype"), 3);

        this.addItem(new Icon(10, new ItemBuilder(Material.NAME_TAG).setName(OwnableColorType.PREFIX.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, target, OwnableColorType.PREFIX).open(player)));

        this.addItem(new Icon(12, new ItemBuilder(Material.WRITABLE_BOOK).setName(OwnableColorType.CHAT.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, target, OwnableColorType.CHAT).open(player)));

        this.addItem(new Icon(14, new ItemBuilder(Material.OAK_HANGING_SIGN).setName(OwnableColorType.NAME.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, target, OwnableColorType.NAME).open(player)));

        this.addItem(new Icon(16, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName(OwnableColorType.LEVEL.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, target, OwnableColorType.LEVEL).open(player)));
    }

}
