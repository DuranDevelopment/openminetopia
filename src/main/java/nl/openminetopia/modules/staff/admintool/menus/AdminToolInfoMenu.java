package nl.openminetopia.modules.staff.admintool.menus;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.TotalStatistic;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.banking.menu.BankContentsMenu;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.menus.ColorTypeMenu;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.player.utils.PlaytimeUtil;
import nl.openminetopia.modules.staff.admintool.menus.fitness.AdminToolFitnessMenu;
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
                .addLoreLine("<gold>Naam: " + minetopiaPlayer.getActiveColor(OwnableColorType.NAME).getColorId() + offlinePlayer.getName())
                .addLoreLine("<gold>Prefix: <dark_gray>[" + minetopiaPlayer.getActiveColor(OwnableColorType.PREFIX).getColorId() + minetopiaPlayer.getActivePrefix().getPrefix() + "<dark_gray>]")
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
            new ColorTypeMenu(player, offlinePlayer).open(player);
        });
        this.addItem(targetColorIcon);

        ItemBuilder levelItemBuilder = new ItemBuilder(Material.TRIPWIRE_HOOK)
                .setName("<gold>Level")
                .addLoreLine("<gold>Level: " + minetopiaPlayer.getLevel())
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om het <yellow>level <gold>van de speler aan te passen.")
                .addLoreLine("")
                .addLoreLine("<yellow>Rechtermuisklik <gold>om het level te verhogen.")
                .addLoreLine("<yellow>Linkermuisklik <gold>om het level te verlagen.");

        Icon targetLevelIcon = new Icon(12, levelItemBuilder.toItemStack(), event -> {
            minetopiaPlayer.setLevel(event.isRightClick() ? minetopiaPlayer.getLevel() + 1 : minetopiaPlayer.getLevel() - 1);
            player.sendMessage(ChatUtils.color("<gold>Je hebt het level van <yellow>" + offlinePlayer.getName() + " <gold>aangepast naar <yellow>" + minetopiaPlayer.getLevel() + "<gold>."));
            new AdminToolInfoMenu(player, offlinePlayer).open(player);
        });
        this.addItem(targetLevelIcon);

        TotalStatistic totalStatistic = (TotalStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL);
        ItemBuilder fitnessItemBuilder = new ItemBuilder(Material.MUSHROOM_STEW)
                .setName("<gold>Fitheid")
                .addLoreLine("<gold>Fitheid: " + totalStatistic.getFitnessGained() + " / " + totalStatistic.getMaxFitnessGainable())
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <yellow>fitheid <gold>van de speler te bekijken.");


        Icon targetFitnessIcon = new Icon(13, fitnessItemBuilder.toItemStack(), event -> {
            new AdminToolFitnessMenu(player, offlinePlayer).open(player);
        });
        this.addItem(targetFitnessIcon);


        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        BankAccountModel accountModel = bankingModule.getAccountsFromPlayer(offlinePlayer.getUniqueId())
                .stream().filter(account -> account.getType() == AccountType.PRIVATE)
                .findFirst().orElse(null);

        if (accountModel == null) {
            player.sendMessage(ChatUtils.color("<red>Er is geen account gevonden voor deze speler."));
            return;
        }

        ItemBuilder bankItemBuilder = new ItemBuilder(Material.GOLD_INGOT)
                .setName("<gold>Banksaldo")
                .addLoreLine("<gold>Banksaldo: " + bankingModule.format(accountModel.getBalance()))
                .addLoreLine("")
                .addLoreLine("<gold>Klik <yellow>hier <gold>om de <yellow>bank <gold>van de speler te openen.");

        Icon targetBankIcon = new Icon(14, bankItemBuilder.toItemStack(), event -> {
            new BankContentsMenu(player, accountModel, true).open(player);
        });
        this.addItem(targetBankIcon);

        ItemBuilder backItemBuilder = new ItemBuilder(Material.OAK_DOOR)
                .setName("<gray>Terug");

        Icon backIcon = new Icon(22, backItemBuilder.toItemStack(), event -> {
            new AdminToolMenu(player, offlinePlayer).open(player);
        });
        this.addItem(backIcon);
    }
}
