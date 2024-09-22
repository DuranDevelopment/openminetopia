package nl.openminetopia.modules.admintool.menus;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.admintool.menus.colors.AdminToolColorMenu;
import nl.openminetopia.modules.player.utils.PlaytimeUtil;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
public class AdminToolInfoMenu extends Menu {

    private final Player player;
    private final OfflinePlayer offlinePlayer;

    public AdminToolInfoMenu(Player player, OfflinePlayer offlinePlayer) {
        super(ChatUtils.color("<gold>Beheerscherm <yellow>" + offlinePlayer.getPlayerProfile().getName()), 3);
        this.player = player;
        this.offlinePlayer = offlinePlayer;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer);
        if (minetopiaPlayer == null) return;

        ItemBuilder skullBuilder = new ItemBuilder(Material.PLAYER_HEAD)
                .setName("<gold>Profielinformatie")
                .addLoreLine(" ")
                .addLoreLine("<gold>UUID: <yellow>" + offlinePlayer.getUniqueId())
                .addLoreLine("<gold>Naam: <yellow>" + offlinePlayer.getName()) // TODO: Set name color
                .addLoreLine("<gold>Prefix: <dark_gray>[" + minetopiaPlayer.getActivePrefixColor().getColor() + minetopiaPlayer.getActivePrefix().getPrefix() + "]")
                .addLoreLine("<gold>Online tijd: <yellow>" + PlaytimeUtil.formatPlaytime(minetopiaPlayer.getPlaytime()))
                .addLoreLine(" ")
                .setSkullOwner(offlinePlayer);

        Icon targetSkullIcon = new Icon(10, skullBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetSkullIcon);

        ItemBuilder colorItemBuilder = new ItemBuilder(Material.YELLOW_CONCRETE)
                .setName("<gold>Kleuren")
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <rainbow>kleuren <gold>van de speler aan te passen.")
                .addLoreLine("");

        Icon targetColorIcon = new Icon(11, colorItemBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            AdminToolColorMenu menu = new AdminToolColorMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());

        });
        this.addItem(targetColorIcon);

        ItemBuilder levelItemBuilder = new ItemBuilder(Material.TRIPWIRE_HOOK)
                .setName("<gold>Level")
                .addLoreLine("<gold>Level: " + minetopiaPlayer.getLevel())
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om het <yellow>level <gold>van de speler aan te passen.");

        Icon targetLevelIcon = new Icon(12, levelItemBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            AdminToolColorMenu menu = new AdminToolColorMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());

        });
        this.addItem(targetLevelIcon);

        ItemBuilder fitnessItemBuilder = new ItemBuilder(Material.MUSHROOM_STEW)
                .setName("<gold>Fitheid")
                .addLoreLine("<gold>Fitheid: " + minetopiaPlayer.getFitness() + " / " + OpenMinetopia.getDefaultConfiguration().getMaxFitnessLevel())
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <yellow>fitheid <gold>van de speler te bekijken.");


        Icon targetFitnessIcon = new Icon(13, fitnessItemBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            AdminToolFitnessMenu menu = new AdminToolFitnessMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());

        });
        this.addItem(targetFitnessIcon);

        ItemBuilder bankItemBuilder = new ItemBuilder(Material.GOLD_INGOT)
                .setName("<gold>Banksaldo")
                .addLoreLine("<gold>Banksaldo: " + "€") // TODO: Add bank balance
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <yellow>bank <gold>van de speler te openen.");

        Icon targetBankIcon = new Icon(14, bankItemBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            AdminToolColorMenu menu = new AdminToolColorMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());

        });
        this.addItem(targetBankIcon);

        ItemBuilder backItemBuilder = new ItemBuilder(Material.OAK_DOOR)
                .setName("<gray>Terug");

        Icon backIcon = new Icon(22, backItemBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            AdminToolMenu menu = new AdminToolMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());

        });
        this.addItem(backIcon);

        this.open(player);
    }
}
