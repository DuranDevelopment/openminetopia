package nl.openminetopia.modules.color.menus;

import com.jazzkuh.inventorylib.objects.PaginatedMenu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ColorSelectMenu extends PaginatedMenu {

    private List<OwnableColor> colors;
    private final OwnableColorType type;

    public ColorSelectMenu(Player player, OwnableColorType type) {
        super(ChatUtils.color(type.getDisplayName() + "<reset> Kleuren menu."), 2);
        this.registerPageSlotsBetween(0, 8);

        this.type = type;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            inventory.close();
            return;
        }

        colors = minetopiaPlayer.getColors().stream()
                .filter(color -> color.getClass().equals(type.correspondingClass()))
                .toList();

        colors.forEach(color -> {
            this.addItem(new Icon(new ItemBuilder(Material.IRON_INGOT).setName(color.displayName()).toItemStack(), (e) -> {
                minetopiaPlayer.setActiveColor(color, type);
                player.sendMessage(ChatUtils.color(type.getDisplayName() + " <reset><gray>Kleur verandert naar: "
                        + color.displayName()));
            }));
        });

        this.addSpecialIcon(new Icon(13, new ItemBuilder(Material.LADDER).setName("<gray>Ga terug.").toItemStack(),
                (e) -> new ColorTypeMenu(player).open(player)));

        this.addSpecialIcon(new Icon(14, new ItemBuilder(Material.BARRIER).setName("<red>Locked.").toItemStack(),
                (e) -> new ColorLockedMenu(player, this).open(player)));
    }

    @Override
    public Icon getPreviousPageItem() {
        return new Icon(9, new ItemBuilder(Material.ARROW)
                .setName("<gold>Vorige pagina")
                .toItemStack(), event -> event.setCancelled(true));
    }

    @Override
    public Icon getNextPageItem() {
        return new Icon(17, new ItemBuilder(Material.ARROW)
                .setName("<gold>Volgende pagina")
                .toItemStack(), event -> event.setCancelled(true));
    }

}
