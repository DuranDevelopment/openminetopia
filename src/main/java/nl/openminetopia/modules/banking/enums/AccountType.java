package nl.openminetopia.modules.banking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum AccountType {
    PRIVATE("<gold>Privérekening</gold>", Material.GOLD_BLOCK, 14),
    COMPANY("<aqua>Bedrijfsrekening</aqua>", Material.DIAMOND_BLOCK, 16),
    SAVINGS("<red>Spaarrekening</red>", Material.REDSTONE_BLOCK, 12),
    GOVERNMENT("<dark_green>Overheidsrekening</dark_green>", Material.EMERALD_BLOCK, 10);

    private final String name;
    private final Material material;
    private final int slot;

}