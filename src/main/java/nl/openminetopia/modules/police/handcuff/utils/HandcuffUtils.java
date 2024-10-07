package nl.openminetopia.modules.police.handcuff.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.item.ItemUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class HandcuffUtils {

    public void applyHandcuffEffects(Player target) {
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        for (String effectString : configuration.getHandcuffEffects()) {
            String[] effect = effectString.split(":");
            String effectName = effect[0].toLowerCase();

            PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(effectName));
            if (potionEffectType == null) {
                System.out.println("Invalid potion effect: " + effectName);
                continue;
            }

            if (effect.length == 1) {
                PotionEffect potionEffect = new PotionEffect(potionEffectType, PotionEffect.INFINITE_DURATION, 0);
                target.addPotionEffect(potionEffect);
                continue;
            }

            int amplifier = Integer.parseInt(effect[1]);
            PotionEffect potionEffect = new PotionEffect(potionEffectType, PotionEffect.INFINITE_DURATION, amplifier);
            target.addPotionEffect(potionEffect);
        }
    }

    public void clearHandcuffEffects(Player target) {
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        for (String effectString : configuration.getHandcuffEffects()) {
            String[] effect = effectString.split(":");
            String effectName = effect[0].toLowerCase();

            PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(effectName));
            if (potionEffectType == null) {
                System.out.println("Invalid potion effect: " + effectName);
                continue;
            }
            target.getActivePotionEffects().forEach(activeEffect -> {
                if (activeEffect.getType().equals(potionEffectType)) {
                    target.removePotionEffect(potionEffectType);
                }
            });
        }
    }

    public boolean isHandcuffItem(ItemStack item) {
        return ItemUtils.isValidItem(item, OpenMinetopia.getDefaultConfiguration().getHandcuffItems());
    }
}
