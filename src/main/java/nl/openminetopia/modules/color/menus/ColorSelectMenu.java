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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ColorSelectMenu extends PaginatedMenu {

    private List<OwnableColor> colors;
    private final OwnableColorType type;

    public ColorSelectMenu(Player player, OfflinePlayer target, OwnableColorType type) {
        super(ChatUtils.color(type.getDisplayName() + "<reset><dark_gray> menu"), 2);
        this.registerPageSlotsBetween(0, 8);

        this.type = type;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(target);
        if (minetopiaPlayer == null) {
            inventory.close();
            return;
        }

        colors = minetopiaPlayer.getColors().stream()
                .filter(color -> color.getClass().equals(type.correspondingClass()))
                .toList();

        colors.forEach(color -> {
            ItemBuilder icon = new ItemBuilder(Material.IRON_INGOT)
                    .addLoreLine("")
                    .setName(color.displayName());

            if (color.getExpiresAt() != -1 && color.getExpiresAt() - System.currentTimeMillis() < -1) icon.addLoreLine("<red>Deze kleur is vervallen.");
            if (color.getExpiresAt() != -1 && color.getExpiresAt() - System.currentTimeMillis() > -1) icon.addLoreLine("<gold>Deze kleur vervalt over <yellow>" + millisToTime(color.getExpiresAt() - System.currentTimeMillis()) + "<gold>.");
            if (color.getExpiresAt() == -1) icon.addLoreLine("<gold>Deze kleur vervalt <yellow>nooit<gold>.");

            this.addItem(new Icon(icon.toItemStack(), (e) -> {
                minetopiaPlayer.setActiveColor(color, type);
                player.sendMessage(ChatUtils.color(type.getDisplayName() + " <reset><gray>veranderd naar: "
                        + color.displayName()));
            }));
        });

        this.addSpecialIcon(new Icon(13, new ItemBuilder(Material.LADDER).setName("<gray>Ga terug").toItemStack(),
                (e) -> new ColorTypeMenu(player, target).open(player)));

        this.addSpecialIcon(new Icon(14, new ItemBuilder(Material.BARRIER).setName("<red>Locked").toItemStack(),
                (e) -> new ColorLockedMenu(player, this).open(player)));
    }

    private String millisToTime(long millis) {
        long hours = millisToHours(millis);
        long minutes = millisToMinutes(millis) - (hours * 60);

        return "<yellow>" + hours + " uur, <yellow>" + minutes + " <gold>minuten en <yellow>" + millisToSeconds(millis) + " <gold>seconden";
    }

    private int millisToHours(long millis) {
        return (int) (millis / 1000 / 60 / 60);
    }

    private int millisToMinutes(long millis) {
        return (int) (millis / 1000 / 60);
    }

    private int millisToSeconds(long millis) {
        return (int) (millis / 1000);
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
