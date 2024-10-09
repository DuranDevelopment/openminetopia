package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import java.util.UUID;
import java.util.regex.Pattern;

@CommandAlias("accounts|account|rekening")
public class BankingCreateCommand extends BaseCommand {

    private final Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_]+$");

    @Subcommand("create")
    @Syntax("<type> <name> <balance>")
    @CommandPermission("openminetopia.banking.create")
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

        if (bankingModule.getAccountByName(name) != null) {
            sender.sendMessage(ChatUtils.color("<red>Er bestaat al een rekening met deze naam."));
            return;
        }

        if (!namePattern.matcher(name).matches()) {
            sender.sendMessage(ChatUtils.color("<red>Rekening naam mag alleen letters, cijfers en underscores bevatten."));
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
            accountModel.initSavingTask();
        }));

    }

}
