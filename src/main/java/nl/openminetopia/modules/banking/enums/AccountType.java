package nl.openminetopia.modules.banking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum AccountType {
    PRIVATE("<gold>", "Privérekening", Material.GOLD_BLOCK,  14),
    COMPANY("<aqua>", "Bedrijfsrekening", Material.DIAMOND_BLOCK, 16),
    SAVINGS("<red>", "Spaarrekening", Material.REDSTONE_BLOCK, 12),
    GOVERNMENT("<dark_green>", "Overheidsrekening", Material.EMERALD_BLOCK, 10);

    private final String color;
    private final String name;
    private final Material material;
    private final int slot;

}