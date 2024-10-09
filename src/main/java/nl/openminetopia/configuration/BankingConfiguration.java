package nl.openminetopia.configuration;

import lombok.Getter;
import nl.openminetopia.utils.ConfigurateConfig;
import org.bukkit.Material;

import java.io.File;

@Getter
public class BankingConfiguration extends ConfigurateConfig {

    private final String economyFormat;
    private final Material atmMaterial;

    private final String typeSelectionTitle;
    private final String accountSelectionTitle;
    private final String accountContentsTitle;

    public BankingConfiguration(File file) {
        super(file, "banking.yml", "");

        this.economyFormat = rootNode.node("banking", "economy-format").getString("#,##0.00");
        this.atmMaterial = Material.getMaterial(rootNode.node("banking", "atm-material").getString("RED_SANDSTONE_STAIRS"));

        this.typeSelectionTitle = rootNode.node("banking", "inventories", "select-type-title").getString("<gray>Select het rekeningtype:");
        this.accountSelectionTitle = rootNode.node("banking", "inventories", "select-account-title").getString("<type_color><type_name>");
        this.accountContentsTitle = rootNode.node("banking", "inventories", "account-contents-title").getString("<type_color><account_name> <reset>| <gold><account_balance>");
    }
}
