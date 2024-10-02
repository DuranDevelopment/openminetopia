package nl.openminetopia.modules.color.menus;

import com.jazzkuh.inventorylib.objects.PaginatedMenu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.components.ColorComponent;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class ColorLockedMenu extends PaginatedMenu {


    public ColorLockedMenu(Player player, ColorSelectMenu oldMenu) {
        super(ChatUtils.color(oldMenu.getType().getDisplayName() + " <reset>Locked Kleuren menu."), 6);
        this.registerPageSlotsBetween(0, 44);

        List<ColorComponent> lockedColors = OpenMinetopia.getColorsConfiguration().lockedColors(oldMenu.getColors());
        lockedColors.forEach(component -> {
            this.addItem(new Icon(new ItemBuilder(Material.IRON_INGOT).setName(component.displayName()).toItemStack()));
        });

        this.addSpecialIcon(new Icon(49, new ItemBuilder(Material.LADDER).setName("<gray>Ga terug.").toItemStack(),
                (e) -> oldMenu.open(player)));
    }

    @Override
    public Icon getPreviousPageItem() {
        return new Icon(45, new ItemBuilder(Material.ARROW)
                .setName("<gold>Vorige pagina")
                .toItemStack(), event -> event.setCancelled(true));
    }

    @Override
    public Icon getNextPageItem() {
        return new Icon(53, new ItemBuilder(Material.ARROW)
                .setName("<gold>Volgende pagina")
                .toItemStack(), event -> event.setCancelled(true));
    }
}
