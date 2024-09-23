package nl.openminetopia.modules.banking;

import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.banking.commands.BankingCommand;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Todo:
 * - Commands (create, delete, adduser, removeuser, setbalance, freeze, pin)
 * - Menu's (type selection, banking inventory)
 * - Fix user permissions
 * - Debitcards
 */

@Getter
public class BankingModule extends Module {

    private List<BankAccountModel> bankAccountModels = new ArrayList<>();

    @Override
    public void enable() {
        OpenMinetopia.getInstance().getLogger().info("Loading bank accounts..");
        loadAccounts().whenComplete((accounts, throwable) -> {
            if (throwable != null) {
                OpenMinetopia.getInstance().getLogger().severe("Something went wrong while trying to load all bank accounts: " + throwable.getMessage());
                return;
            }

            bankAccountModels = accounts;
            OpenMinetopia.getInstance().getLogger().info("Loaded a total of " + bankAccountModels.size() + " accounts.");
        });

        registerCommand(new BankingCommand());
    }

    @Override
    public void disable() {

    }

    // fix when permissions are done.
    public List<BankAccountModel> getAccountsFromPlayer(UUID uuid) {
        return null;
    }

    public BankAccountModel getAccountByName(String name) {
        return bankAccountModels.stream().filter(account -> account.getName().equals(name)).findAny().orElse(null);
    }

    public BankAccountModel getAccountById(UUID uuid) {
        return bankAccountModels.stream().filter(account -> account.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    public CompletableFuture<List<BankAccountModel>> loadAccounts() {
        CompletableFuture<List<BankAccountModel>> completableFuture = new CompletableFuture<>();
        try {
            CompletableFuture<Collection<BankAccountModel>> modelFuture = StormDatabase.getInstance().getStorm().buildQuery(BankAccountModel.class)
                    .where("type", Where.NOT_EQUAL, AccountType.PRIVATE.toString())
                    .execute();

            modelFuture.whenComplete((models, throwable) -> {
                if (throwable != null) {
                    completableFuture.completeExceptionally(throwable);
                    return;
                }

                completableFuture.complete(new ArrayList<>(models));
            });

        } catch (Exception e) {
            completableFuture.completeExceptionally(e);
        }
        return completableFuture;
    }


}
