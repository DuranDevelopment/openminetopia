package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.data.utils.StormUtils;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

@CommandAlias("accounts|account|rekening")
public class BankingFreezeCommand extends BaseCommand {

    @Subcommand("freeze")
    @CommandCompletion("@accountNames")
    @CommandPermission("openminetopia.banking.freeze")
    public void freezeAccount(CommandSender sender, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(MessageConfiguration.component("banking_account_not_found"));
            return;
        }

        boolean newState = !accountModel.getFrozen();

        CompletableFuture<Void> updateFuture = StormUtils.updateModelData(BankAccountModel.class,
                query -> query.where("uuid", Where.EQUAL, accountModel.getUniqueId().toString()),
                updateModel -> updateModel.setFrozen(newState)
        );

        updateFuture.whenComplete((v, throwable) -> {
            if(throwable != null) {
                sender.sendMessage(MessageConfiguration.component("database_update_error"));
                return;
            }
            accountModel.setFrozen(newState);
            accountModel.save();

            if (newState) {
                // TODO: Replace <account_name> with the actual value
                sender.sendMessage(MessageConfiguration.component("banking_account_frozen"));
                return;
            }
            // TODO: Replace <account_name> with the actual value
            sender.sendMessage(MessageConfiguration.component("banking_account_unfrozen"));
        });

    }

}
