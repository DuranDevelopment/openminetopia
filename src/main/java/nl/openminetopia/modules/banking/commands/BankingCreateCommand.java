package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@CommandAlias("accounts|account|rekening")
public class BankingCreateCommand extends BaseCommand {

    @Subcommand("create")
    @Syntax("<type> <name> <balance>")
    public void createAccount(CommandSender sender, AccountType type, String name) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);

        if(type == AccountType.PRIVATE) {
            sender.sendMessage(ChatUtils.color("<red>Je mag geen prive rekening aanmaken, deze zijn uniek per player."));
            return;
        }

        if (name.contains(" ")) {
            sender.sendMessage(ChatUtils.color("<red>Rekening naam mag geen spatie bevatten."));
            return;
        }

        if (name.length() < 3 || name.length() > 24) {
            sender.sendMessage(ChatUtils.color("<red>Rekening naam moet minimaal 3 en maximaal 24 characters bevatten."));
            return;
        }

        UUID accountId = UUID.randomUUID();
        BankAccountModel accountModel = new BankAccountModel();
        accountModel.setUniqueId(accountId);
        accountModel.setType(type);
        accountModel.setName(name);
        accountModel.setBalance(0L);
        accountModel.setFrozen(false);
        accountModel.setUsers(new HashMap<>());

        try {
            bankingModule.createAccount(accountModel);
            sender.sendMessage(ChatUtils.color("<gold>Je hebt de rekening</gold> <red>" + accountModel.getName() + "</red> <gold>aangemaakt."));
        } catch (SQLException e) {
            sender.sendMessage(ChatUtils.color("<red>Er ging iets fout met het aanmaken van de rekening."));
            OpenMinetopia.getInstance().getLogger().severe("Something went wrong while trying to create an account: " + e.getMessage());
        }
    }

    @Subcommand("listAll")
    public void listAll(CommandSender sender) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);

        for (BankAccountModel bankAccountModel : bankingModule.getBankAccountModels()) {
            sender.sendMessage(bankAccountModel.getName());
            sender.sendMessage("  - Balance:" + bankAccountModel.getBalance());
            sender.sendMessage("  - Users:");

            bankAccountModel.getUsers().forEach((uuid, permission) -> {
                sender.sendMessage("    " + uuid + ": " + permission);
            });
        }
    }

}
