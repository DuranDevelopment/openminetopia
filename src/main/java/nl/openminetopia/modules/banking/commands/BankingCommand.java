package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@CommandAlias("accounts|account|rekening")
public class BankingCommand extends BaseCommand {

    @Default
    @Syntax("<type> <name> <balance>")
    public void test(CommandSender sender, AccountType type, String name, long balance) {
        sender.sendMessage("executing.");

        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setUniqueId(UUID.randomUUID());
        bankAccountModel.setBalance(balance);
        bankAccountModel.setName(name);
        bankAccountModel.setType(type);

        StormDatabase.getInstance().saveStormModel(bankAccountModel);
    }

}
