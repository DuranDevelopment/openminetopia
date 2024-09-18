package nl.openminetopia.modules.fitness.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.fitness.objects.FitnessLevel;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class FitnessUtils {

    public static void applyFitness(Player player) {
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        int totalFitness = minetopiaPlayer.getFitness();
        double walkSpeed = 0.2;

        List<PotionEffectType> possibleEffects = new ArrayList<>();
        Map<PotionEffectType, Integer> effects = new HashMap<>();

        for (Map.Entry<String, FitnessLevel> entry : configuration.getFitnessLevels().entrySet()) {
            int minFitness = Integer.parseInt(entry.getKey().split("-")[0]);
            int maxFitness = Integer.parseInt(entry.getKey().split("-")[1]);

            entry.getValue().getEffects().forEach(effect -> {
                String effectName = effect.split(":")[0].toLowerCase();
                if (!possibleEffects.contains(Registry.EFFECT.get(NamespacedKey.minecraft(effectName))))
                    possibleEffects.add(Registry.EFFECT.get(NamespacedKey.minecraft(effectName)));
            });

            if (totalFitness >= minFitness && totalFitness <= maxFitness) {
                walkSpeed = entry.getValue().getWalkSpeed();
                entry.getValue().getEffects().forEach(effect -> {
                    String effectName = effect.split(":")[0].toLowerCase();
                    int effectAmplifier = Integer.parseInt(effect.split(":")[1]);
                    PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(effectName));
                    if (potionEffectType == null) {
                        OpenMinetopia.getInstance().getLogger().warning("Could not find potion effect type for " + effectName);
                        return;
                    }

                    effects.put(potionEffectType, effectAmplifier);
                });
            }
        }

        float finalWalkSpeed = (float) walkSpeed;
        Bukkit.getScheduler().runTask(OpenMinetopia.getInstance(), () -> {
            if (player.getWorld().hasStorm() && configuration.isRainSlowdownEnabled()) {
                if (player.getWalkSpeed() != finalWalkSpeed - 0.05f) {
                    player.setWalkSpeed(finalWalkSpeed - 0.05f);
                }
            }
            player.setWalkSpeed(finalWalkSpeed);

            if (effects.isEmpty()) {
                possibleEffects.forEach(potionEffect -> {
                    if (player.hasPotionEffect(potionEffect)) player.removePotionEffect(potionEffect);
                });
            }

            effects.forEach((effect, amplifier) -> {
                if (player.hasPotionEffect(effect)) {
                    player.removePotionEffect(effect);
                }
                player.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, amplifier - 1, true, false));
            });
        });
    }
}
