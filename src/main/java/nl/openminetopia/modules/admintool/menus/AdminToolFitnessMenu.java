package nl.openminetopia.modules.admintool.menus;

import com.jazzkuh.inventorylib.objects.Menu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.admintool.menus.colors.AdminToolColorMenu;
import nl.openminetopia.modules.player.utils.PlaytimeUtil;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@Getter
public class AdminToolFitnessMenu extends Menu {

    private final Player player;
    private final OfflinePlayer offlinePlayer;

    public AdminToolFitnessMenu(Player player, OfflinePlayer offlinePlayer) {
        super(ChatUtils.color("<gold>Fitheid <yellow>" + offlinePlayer.getPlayerProfile().getName()), 3);
        this.player = player;
        this.offlinePlayer = offlinePlayer;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer);
        if (minetopiaPlayer == null) return;

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        ItemBuilder drinkingItemBuilder = new ItemBuilder(Material.POTION)
                .setName("<gold>Drinken " + minetopiaPlayer.getFitness().getFitnessGainedByDrinking() + "/" + configuration.getMaxFitnessByDrinking())
                .addLoreLine(" ")
                .addLoreLine("<gold>Precieze score: <yellow>" + minetopiaPlayer.getFitness().getDrinkingPoints())
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>" + configuration.getDrinkingPointsPerWaterBottle() + " <dark_purple>punt voor het drinken van water.")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>" + configuration.getDrinkingPointsPerPotion() + " <dark_purple>punt voor het drinken van potions.")
                .addLoreLine(" ");

        Icon targetDrinkingIcon = new Icon(9, drinkingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetDrinkingIcon);

        ItemBuilder eatingItemBuilder = new ItemBuilder(Material.APPLE)
                .setName("<gold>Fatsoenlijk eten " + minetopiaPlayer.getFitness().getFitnessGainedByHealth() + "/" + configuration.getMaxFitnessByHealth())
                .addLoreLine(" ")
                .addLoreLine("<gold>Precieze score: <yellow>" + minetopiaPlayer.getFitness().getHealthPoints())
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>" + configuration.getPointsAbove9Hearts() + " <dark_purple>punt als tijdens de")
                .addLoreLine("check hun voedselniveau hoger is dan <light_purple>9")
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers verliezen <light_purple>" + configuration.getPointsBelow5Hearts() + " <dark_purple>punt als tijdens de")
                .addLoreLine("check hun voedselniveau lager is dan <light_purple>5")
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers verliezen <light_purple>" + configuration.getPointsBelow2Hearts() + " <dark_purple>punt als tijdens de")
                .addLoreLine("check hun voedselniveau lager is dan <light_purple>2")
                .addLoreLine(" ");

        Icon targetEatingIcon = new Icon(10, eatingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetEatingIcon);

        ItemBuilder foodItemBuilder = new ItemBuilder(Material.GOLDEN_APPLE)
                .setName("<gold>Eten " + minetopiaPlayer.getFitness().getFitnessGainedByHealth() + "/" + configuration.getMaxFitnessByHealth())
                .addLoreLine(" ")
                .addLoreLine("<gold>Luxe eten genuttigd: <yellow>" + minetopiaPlayer.getFitness().getHealthPoints())
                .addLoreLine("<gold>Goedkoop eten genuttigd: <yellow>" + minetopiaPlayer.getFitness().getHealthPoints())
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Luxe eten:")
                .addLoreLine("<light_purple>cooked beef, ...")
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Goedkoop eten:")
                .addLoreLine("<light_purple>apple, ...")
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>" + configuration.getPointsAbove9Hearts() + " <dark_purple>voor het eten van luxe voedsel.")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>" + configuration.getPointsBelow5Hearts() + " <dark_purple>voor het eten van goedkoop voedsel.")
                .addLoreLine(" ");

        Icon targetFoodIcon = new Icon(11, foodItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetFoodIcon);

        ItemBuilder climbingItemBuilder = new ItemBuilder(Material.LADDER)
                .setName("<gold>Klimmen " + minetopiaPlayer.getFitness().getFitnessGainedByClimbing() + "/" + configuration.getMaxFitnessByClimbing())
                .addLoreLine(" ")
                .addLoreLine("<gold>Kilometers geklommen: <yellow>" + (minetopiaPlayer.getBukkit().getStatistic(Statistic.CLIMB_ONE_CM) / 1000))
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>1 <dark_purple>punt per <light_purple>" + (configuration.getCmPerClimbingPoint() / 1000) + " <dark_purple>kilometer klimmen.")
                .addLoreLine(" ");

        Icon targetClimbingIcon = new Icon(12, climbingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetClimbingIcon);

        ItemBuilder flyingItemBuilder = new ItemBuilder(Material.ELYTRA)
                .setName("<gold>Vliegen " + minetopiaPlayer.getFitness().getFitnessGainedByFlying() + "/" + configuration.getMaxFitnessByFlying())
                .addLoreLine(" ")
                .addLoreLine("<gold>Kilometers gevlogen: <yellow>" + (minetopiaPlayer.getBukkit().getStatistic(Statistic.AVIATE_ONE_CM) / 1000))
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>1 <dark_purple>punt per <light_purple>" + (configuration.getCmPerFlyingPoint() / 1000) + " <dark_purple>kilometer vliegen.")
                .addLoreLine(" ");

        Icon targetFlyingIcon = new Icon(13, flyingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetFlyingIcon);

        ItemBuilder walkingItemBuilder = new ItemBuilder(Material.LEATHER_BOOTS)
                .setName("<gold>Lopen " + minetopiaPlayer.getFitness().getFitnessGainedByWalking() + "/" + configuration.getMaxFitnessByWalking())
                .addLoreLine(" ")
                .addLoreLine("<gold>Kilometers gelopen: <yellow>" + (minetopiaPlayer.getBukkit().getStatistic(Statistic.WALK_ONE_CM) / 1000))
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>1 <dark_purple>punt per <light_purple>" + (configuration.getCmPerWalkingPoint() / 1000) + " <dark_purple>kilometer lopen.")
                .addLoreLine(" ");

        Icon targetWalkingIcon = new Icon(14, walkingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetWalkingIcon);

        ItemBuilder swimmingItemBuilder = new ItemBuilder(Material.OAK_BOAT)
                .setName("<gold>Zwemmen " + minetopiaPlayer.getFitness().getFitnessGainedBySwimming() + "/" + configuration.getMaxFitnessBySwimming())
                .addLoreLine(" ")
                .addLoreLine("<gold>Kilometers gezwommen: <yellow>" + (minetopiaPlayer.getBukkit().getStatistic(Statistic.SWIM_ONE_CM) / 1000))
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>1 <dark_purple>punt per <light_purple>" + (configuration.getCmPerWalkingPoint() / 1000) + " <dark_purple>kilometer zwemmen.")
                .addLoreLine(" ");

        Icon targetSwimmingIcon = new Icon(15, swimmingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetSwimmingIcon);

        ItemBuilder sprintingItemBuilder = new ItemBuilder(Material.DIAMOND_BOOTS)
                .setName("<gold>Rennen " + minetopiaPlayer.getFitness().getFitnessGainedBySprinting() + "/" + configuration.getMaxFitnessBySprinting())
                .addLoreLine(" ")
                .addLoreLine("<gold>Kilometers gerend: <yellow>" + (minetopiaPlayer.getBukkit().getStatistic(Statistic.SPRINT_ONE_CM) / 1000))
                .addLoreLine(" ")
                .addLoreLine("<dark_purple>Spelers krijgen <light_purple>1 <dark_purple>punt per <light_purple>" + (configuration.getCmPerSprintingPoint() / 1000) + " <dark_purple>kilometer rennen.")
                .addLoreLine(" ");

        Icon targetSprintingIcon = new Icon(16, sprintingItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetSprintingIcon);

        ItemBuilder totalItemBuilder = new ItemBuilder(Material.PAPER)
                .setName("<gold>Totaal: <yellow>" + minetopiaPlayer.getFitness().getTotalFitness() + "<gold>/<yellow>" + configuration.getMaxFitnessLevel());

        Icon targetTotalIcon = new Icon(17, totalItemBuilder.toItemStack(), event -> event.setCancelled(true));
        this.addItem(targetTotalIcon);

        ItemBuilder backItemBuilder = new ItemBuilder(Material.OAK_DOOR)
                .setName("<gray>Terug");

        Icon backIcon = new Icon(22, backItemBuilder.toItemStack(), event -> {
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer == null) return;
            AdminToolInfoMenu menu = new AdminToolInfoMenu(player, offlinePlayer);
            menu.open((Player) event.getWhoClicked());

        });
        this.addItem(backIcon);

        this.open(player);
    }
}
