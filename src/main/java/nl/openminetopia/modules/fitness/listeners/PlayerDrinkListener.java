package nl.openminetopia.modules.fitness.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class PlayerDrinkListener implements Listener {

    @EventHandler
    public void onPlayerDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() != Material.POTION) return;

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(event.getPlayer());
        if (minetopiaPlayer == null) return;

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        // check if player drank water less than 5 minutes ago

        // get drinking cooldown from minutes in millis
        long drinkingCooldown = configuration.getDrinkingCooldown() * 60000L;
        if (minetopiaPlayer.getLastDrinkingTime() + drinkingCooldown > System.currentTimeMillis()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Je moet nog even wachten voor je weer kunt drinken.");
            return;
        }

        double currentDrinkingPoints = minetopiaPlayer.getDrinkingPoints(); // Huidige drink punten
        double drinkingPointsPerBottle = configuration.getDrinkingPointsPerWaterBottle();

        if (meta.getBasePotionType() == PotionType.WATER) {
            event.getPlayer().sendMessage("Je hebt water gedronken.");
            minetopiaPlayer.setDrinkingPoints(currentDrinkingPoints + drinkingPointsPerBottle);
            minetopiaPlayer.setLastDrinkingTime(System.currentTimeMillis());
            return;
        }
        double drinkingPointsPerPotion = configuration.getDrinkingPointsPerPotion();
        event.getPlayer().sendMessage("Je hebt een potion gedronken.");
        minetopiaPlayer.setDrinkingPoints(currentDrinkingPoints + drinkingPointsPerPotion);
        minetopiaPlayer.setLastDrinkingTime(System.currentTimeMillis());
    }
}
