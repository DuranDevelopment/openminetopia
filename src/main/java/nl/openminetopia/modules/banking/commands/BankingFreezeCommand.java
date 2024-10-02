package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.utils.StormUtils;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

@CommandAlias("accounts|account|rekening")
public class BankingFreezeCommand extends BaseCommand {

    @Subcommand("freeze")
    public void freezeAccount(CommandSender sender, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        boolean newState = !accountModel.getFrozen();

        CompletableFuture<Void> updateFuture = StormUtils.updateModelData(BankAccountModel.class,
                query -> query.where("uuid", Where.EQUAL, accountModel.getUniqueId().toString()),
                updateModel -> updateModel.setFrozen(newState)
        );

        updateFuture.whenComplete((v, throwable) -> {
            if(throwable != null) {
                sender.sendMessage(ChatUtils.color("<red>Er ging iets mis met het updaten van de database informatie."));
                return;
            }
            accountModel.setFrozen(newState);

            if (newState) {
                sender.sendMessage(ChatUtils.color("<gold>Het account <red>" + accountModel.getName() + " <gold>is nu bevroren."));
                return;
            }

            sender.sendMessage(ChatUtils.color("<gold>Het account <red>" + accountModel.getName() + " <gold>is nu niet langer bevroren."));
        });

    }

}
