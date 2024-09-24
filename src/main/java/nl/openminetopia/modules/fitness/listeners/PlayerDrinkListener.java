package nl.openminetopia.modules.fitness.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.Objects;

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
        if (minetopiaPlayer.getFitness().getLastDrinkingTime() + drinkingCooldown > System.currentTimeMillis()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Je moet nog even wachten voor je weer kunt drinken.");
            return;
        }

        double currentDrinkingPoints = minetopiaPlayer.getFitness().getDrinkingPoints(); // Huidige drink punten
        double drinkingPointsPerBottle = configuration.getDrinkingPointsPerWaterBottle();

        if (meta.getBasePotionType() == PotionType.WATER) {
            event.getPlayer().sendMessage("Je hebt water gedronken.");
            minetopiaPlayer.getFitness().setDrinkingPoints(currentDrinkingPoints + drinkingPointsPerBottle);
            minetopiaPlayer.getFitness().setLastDrinkingTime(System.currentTimeMillis());
        } else {
            double drinkingPointsPerPotion = configuration.getDrinkingPointsPerPotion();
            event.getPlayer().sendMessage("Je hebt een potion gedronken.");
            minetopiaPlayer.getFitness().setDrinkingPoints(currentDrinkingPoints + drinkingPointsPerPotion);
            minetopiaPlayer.getFitness().setLastDrinkingTime(System.currentTimeMillis());
        }

        if (minetopiaPlayer.getFitness().getDrinkingPoints() >= 1 && minetopiaPlayer.getFitness().getFitnessGainedByDrinking() <= configuration.getMaxFitnessByDrinking()) {
            minetopiaPlayer.getFitness().setFitnessGainedByDrinking(minetopiaPlayer.getFitness().getFitnessGainedByDrinking() + 1);
            minetopiaPlayer.getFitness().setDrinkingPoints(0);
        }
    }
}
