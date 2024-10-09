package nl.openminetopia.modules.banking;

import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.banking.commands.*;
import nl.openminetopia.modules.banking.listeners.BankingInteractionListener;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.data.storm.models.BankPermissionModel;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Todo:
 * - Menu's (banking inventory)
 * - Debitcards
 */

@Getter
public class BankingModule extends Module {

    private DecimalFormat decimalFormat;
    private Collection<BankAccountModel> bankAccountModels = new ArrayList<>();

    @Override
    public void enable() {
        decimalFormat = new DecimalFormat(OpenMinetopia.getBankingConfiguration().getEconomyFormat());

        Bukkit.getScheduler().runTaskLater(OpenMinetopia.getInstance(), () -> {
            OpenMinetopia.getInstance().getLogger().info("Loading bank accounts..");
            DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
            dataModule.getAdapter().getBankAccounts().whenComplete((accounts, accountThrowable) -> {
                if (accountThrowable != null) {
                    OpenMinetopia.getInstance().getLogger().severe("Something went wrong while trying to load all bank accounts: " + accountThrowable.getMessage());
                    return;
                }

                bankAccountModels = accounts;
                bankAccountModels.forEach(BankAccountModel::initSavingTask);

                OpenMinetopia.getInstance().getLogger().info("Loaded a total of " + bankAccountModels.size() + " accounts.");

                dataModule.getAdapter().getBankPermissions().whenComplete((permissions, throwable) -> {
                    if (throwable != null) {
                        OpenMinetopia.getInstance().getLogger().severe("Something went wrong while trying to load all bank permissions: " + throwable.getMessage());
                        return;
                    }

                    permissions.forEach(permission -> {
                        BankAccountModel accountModel = getAccountById(permission.getAccount());
                        if (accountModel == null) {
                            /*
                            todo: remove permission from db?
                             dataModule.getAdapter().deleteBankPermission(permission.getAccount(), permission.getUuid());
                             */
                            return;
                        }

                        accountModel.getUsers().put(permission.getUuid(), permission.getPermission());
                    });

                    OpenMinetopia.getInstance().getLogger().info("Found and applied " + permissions.size() + " bank permissions.");
                });

            });
        }, 3L);

        OpenMinetopia.getCommandManager().getCommandCompletions().registerCompletion("accountNames", context -> bankAccountModels.stream().map(BankAccountModel::getName).collect(Collectors.toList()));

        registerCommand(new BankingCreateCommand());
        registerCommand(new BankingDeleteCommand());
        registerCommand(new BankingUsersCommand());
        registerCommand(new BankingOpenCommand());
        registerCommand(new BankingFreezeCommand());
        registerCommand(new BankingInfoCommand());
        registerCommand(new BankingBalanceCommand());
        registerCommand(new BankingListCommand());

        registerListener(new BankingInteractionListener());
    }

    @Override
    public void disable() {
        bankAccountModels.forEach(accountModel -> {
            if (accountModel.getSavingTask() != null) {
                accountModel.getSavingTask().saveAndCancel();
            }
        });
    }

    public List<BankAccountModel> getAccountsFromPlayer(UUID uuid) {
        return bankAccountModels.stream().filter(account -> account.getUsers().containsKey(uuid)).collect(Collectors.toList());
    }

    public BankAccountModel getAccountByName(String name) {
        return bankAccountModels.stream().filter(account -> account.getName().equals(name)).findAny().orElse(null);
    }

    public BankAccountModel getAccountById(UUID uuid) {
        return bankAccountModels.stream().filter(account -> account.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    @SneakyThrows
    public CompletableFuture<BankAccountModel> getAccountModel(UUID accountId) {
        CompletableFuture<BankAccountModel> accountModelFuture = new CompletableFuture<>();

        Collection<BankAccountModel> collectionFuture = StormDatabase.getInstance().getStorm()
                .buildQuery(BankAccountModel.class)
                .where("uuid", Where.EQUAL, accountId.toString())
                .execute().join();

        accountModelFuture.complete(collectionFuture.stream().findFirst().orElse(null));

        return accountModelFuture;
    }

    public void createPermissions(BankPermissionModel permissionModel) throws SQLException {
        BankAccountModel accountModel = getAccountById(permissionModel.getAccount());
        accountModel.getUsers().put(permissionModel.getUuid(), permissionModel.getPermission());
        StormDatabase.getInstance().getStorm().save(permissionModel);
    }

    public String format(double amount) {
        return "€" + decimalFormat.format(amount);
    }

}
