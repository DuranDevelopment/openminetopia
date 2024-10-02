package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@CommandAlias("accounts|account|rekening")
public class BankingCreateCommand extends BaseCommand {

    @Subcommand("create")
    @Syntax("<type> <name> <balance>")
    public void createAccount(CommandSender sender, AccountType type, String name) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

        if (type == AccountType.PRIVATE) {
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
        dataModule.getAdapter().createBankAccount(accountId, type, 0L, name, false).whenComplete(((accountModel, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(ChatUtils.color("<red>Er ging iets fout met het aanmaken van de rekening."));
                OpenMinetopia.getInstance().getLogger().severe("Something went wrong while trying to create an account: " + throwable.getMessage());
            }

            sender.sendMessage(ChatUtils.color("<gold>Je hebt de rekening</gold> <red>" + accountModel.getName() + "</red> <gold>aangemaakt."));
            bankingModule.getBankAccountModels().add(accountModel);
        }));

    }

}
