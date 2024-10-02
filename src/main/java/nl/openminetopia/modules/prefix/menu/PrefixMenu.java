package nl.openminetopia.modules.prefix.menu;

import com.jazzkuh.inventorylib.objects.PaginatedMenu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrefixMenu extends PaginatedMenu {

    private final OfflinePlayer offlinePlayer;
    private final Player player;

    public PrefixMenu(Player player, OfflinePlayer offlinePlayer) {
        super(ChatUtils.color("<black>Kies een prefix"), 2);
        this.registerPageSlotsBetween(0, 9);
        this.offlinePlayer = offlinePlayer;
        this.player = player;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer);
        if (minetopiaPlayer == null) return;

        List<Prefix> prefixes = new ArrayList<>(minetopiaPlayer.getPrefixes());

        if (minetopiaPlayer.getActivePrefix() != null && minetopiaPlayer.getActivePrefix().getId() != -1) {
            prefixes.add(new Prefix(-1, OpenMinetopia.getDefaultConfiguration().getDefaultPrefix(), -1));
        }

        prefixes.removeIf(prefix -> prefix.getId() == minetopiaPlayer.getActivePrefix().getId());

        Icon selectedPrefix = new Icon(0, new ItemBuilder(Material.NAME_TAG)
                .setName("<white>" + minetopiaPlayer.getActivePrefix().getPrefix())
                .addLoreLine("")
                .addLoreLine("<gold>Je hebt deze prefix geselecteerd.")
                .setGlowing(true)
                .toItemStack(),
                event -> event.setCancelled(true));
        this.addItem(selectedPrefix);

        int i = 1;
        for (Prefix prefix : prefixes) {
            var builder = new ItemBuilder(Material.PAPER)
                    .setName("<white>" + prefix.getPrefix())
                    .addLoreLine("")
                    .addLoreLine("<gold>Klik <yellow>hier <gold>om deze prefix te selecteren.")
                    .addLoreLine("");

            if (prefix.getExpiresAt() != -1 && prefix.getExpiresAt() - System.currentTimeMillis() < -1) builder.addLoreLine("<red>Deze prefix is vervallen.");
            if (prefix.getExpiresAt() != -1 && prefix.getExpiresAt() - System.currentTimeMillis() > -1) builder.addLoreLine("<gold>Deze prefix vervalt over <yellow>" + millisToTime(prefix.getExpiresAt() - System.currentTimeMillis()) + "<gold>.");
            if (prefix.getExpiresAt() == -1) builder.addLoreLine("<gold>Deze prefix vervalt <yellow>nooit<gold>.");

            Icon prefixIcon = new Icon(i, builder.toItemStack(),
                    event -> {
                        event.setCancelled(true);
                        minetopiaPlayer.setActivePrefix(prefix.getId() == -1 ? new Prefix(-1, OpenMinetopia.getDefaultConfiguration().getDefaultPrefix(), -1) : prefix);
                        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<gold>Je hebt de prefix <yellow>" + prefix.getPrefix() + " <gold>geselecteerd."));
                        new PrefixMenu(player, offlinePlayer).open(player);
                    });
            this.addItem(prefixIcon);
            i++;
        }
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
        return new Icon(12, new ItemBuilder(Material.ARROW)
                .setName("<gold>Vorige pagina")
                .toItemStack(), event -> {
            event.setCancelled(true);
        });
    }

    @Override
    public Icon getNextPageItem() {
        return new Icon(14, new ItemBuilder(Material.ARROW)
                .setName("<gold>Volgende pagina")
                .toItemStack(), event -> {
            event.setCancelled(true);
        });
    }
}
