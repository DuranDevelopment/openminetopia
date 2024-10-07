package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

@CommandAlias("accounts|account|rekening")
public class BankingDeleteCommand extends BaseCommand {

    @Subcommand("delete")
    @Syntax("<name>")
    @CommandCompletion("@accountNames")
    @CommandPermission("openminetopia.banking.delete")
    public void deleteAccount(CommandSender sender, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        dataModule.getAdapter().deleteBankAccount(accountModel.getUniqueId()).whenComplete((v, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(ChatUtils.color("<red>Er ging iets fout tijdens het verwijderen van het account."));
                return;
            }

            sender.sendMessage(ChatUtils.color("<gold>Het account</gold> <red>" + accountModel.getName() + "</red> <gold>is verwijderd."));
            bankingModule.getBankAccountModels().remove(accountModel);
        });
    }

}
