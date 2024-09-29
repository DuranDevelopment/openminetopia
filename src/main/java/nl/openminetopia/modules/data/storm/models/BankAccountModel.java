package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.banking.enums.AccountType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Table(name = "banking_accounts")
public class BankAccountModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;

    @Column(name = "type", defaultValue = "PRIVATE")
    private AccountType type;

    @Column(name = "balance", defaultValue = "0")
    private Long balance;

    @Column(name = "name", defaultValue = "Rekening")
    private String name;

    @Column(name = "frozen", defaultValue = "false")
    private Boolean frozen;

    private Map<UUID, AccountPermission> users = new HashMap<>();

    public boolean hasPermission(UUID uuid, AccountPermission accountPermission) {
        if(!users.containsKey(uuid)) return false;
        AccountPermission currentPermission = users.get(uuid);
        return currentPermission == AccountPermission.ADMIN || currentPermission == accountPermission;
    }

}
