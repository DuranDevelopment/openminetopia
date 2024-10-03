package nl.openminetopia.modules.color.menus;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ColorTypeMenu extends Menu {
    public ColorTypeMenu(Player player) {
        super(ChatUtils.color("<gray>Kies uw kleurtype."), 3);

        this.addItem(new Icon(10, new ItemBuilder(Material.RED_WOOL).setName(OwnableColorType.PREFIX.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, OwnableColorType.PREFIX).open(player)));

        this.addItem(new Icon(12, new ItemBuilder(Material.LIME_WOOL).setName(OwnableColorType.CHAT.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, OwnableColorType.CHAT).open(player)));

        this.addItem(new Icon(14, new ItemBuilder(Material.BLUE_WOOL).setName(OwnableColorType.NAME.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, OwnableColorType.NAME).open(player)));

        this.addItem(new Icon(16, new ItemBuilder(Material.PINK_WOOL).setName(OwnableColorType.LEVEL.getDisplayName()).toItemStack(),
                (e) -> new ColorSelectMenu(player, OwnableColorType.LEVEL).open(player)));
    }

}
