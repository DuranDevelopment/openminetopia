package nl.openminetopia.modules.banking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum AccountType {
    PRIVATE("Privérekening", Material.GOLD_BLOCK),
    COMPANY("Bedrijfsrekening", Material.DIAMOND_BLOCK),
    SAVINGS("Spaarrekening", Material.REDSTONE_BLOCK),
    GOVERNMENT("Overheidsrekening", Material.EMERALD_BLOCK);

    private final String name;
    private final Material material;

}