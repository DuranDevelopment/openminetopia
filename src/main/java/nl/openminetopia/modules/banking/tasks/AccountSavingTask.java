package nl.openminetopia.modules.banking.tasks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@RequiredArgsConstructor
public class AccountSavingTask extends BukkitRunnable {

    private final BankAccountModel account;

    @Override
    public void run() {
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
        dataModule.getAdapter().saveBankAccount(account);
    }
}
