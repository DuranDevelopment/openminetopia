package nl.openminetopia.modules.detectiongates;

import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.detectiongates.listeners.DetectionListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DetectionModule extends Module {

    private final Map<Location, Material> blocks = new HashMap<>();

    @Override
    public void enable() {
        registerListener(new DetectionListener());
    }

    @Override
    public void disable() {
        blocks.forEach((location, material) -> location.getBlock().setType(material));
    }

    public List<ItemStack> getFlaggedItems(Player player) {
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        return Arrays.stream(player.getInventory().getContents())
                .filter(item -> item != null)
                .filter(item -> !item.getType().isAir())
                .filter(item -> configuration.getDetectionMaterials().contains(item.getType()))
                .toList();
    }

}
