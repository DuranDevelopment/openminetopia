package nl.openminetopia.modules.player.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        PlayerManager.getInstance().getPlayerModels().remove(event.getUniqueId());

        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

        try {
            dataModule.getAdapter().loadPlayer(event.getUniqueId());
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
            OpenMinetopia.getInstance().getLogger().warning("Error loading player model: " + e.getMessage());
        }


        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        bankingModule.getAccountModel(event.getUniqueId()).whenComplete(((bankAccountModel, throwable) -> {
            if (throwable != null) {
                OpenMinetopia.getInstance().getLogger().info("Could not account for: " + throwable.getMessage());
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er ging iets fout tijdens het ophalen van je bank gegevens."));
                return;
            }

            if (bankAccountModel == null) {
                OpenMinetopia.getInstance().getLogger().info("account is null, creating.");

                dataModule.getAdapter().createBankAccount(event.getUniqueId(), AccountType.PRIVATE, 0L, event.getName(), false).whenComplete((accountModel, accountThrowable) -> {
                    if (accountThrowable != null) {
                        OpenMinetopia.getInstance().getLogger().severe("Couldn't create account for " + event.getName() + ": " + accountThrowable.getMessage());
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er ging iets fout tijdens het ophalen van je bank gegevens."));
                    }

                    accountModel.initSavingTask();
                    accountModel.getUsers().put(event.getUniqueId(), AccountPermission.ADMIN);
                    bankingModule.getBankAccountModels().add(accountModel);
                    OpenMinetopia.getInstance().getLogger().info("Loaded account for: " + event.getName() + " (" + accountModel + ")");
                });

                return;
            }

            OpenMinetopia.getInstance().getLogger().info("account is not null, loading.");

            bankAccountModel.getUsers().put(event.getUniqueId(), AccountPermission.ADMIN);
            bankAccountModel.initSavingTask();
            bankingModule.getBankAccountModels().add(bankAccountModel);
        }));
    }
}
