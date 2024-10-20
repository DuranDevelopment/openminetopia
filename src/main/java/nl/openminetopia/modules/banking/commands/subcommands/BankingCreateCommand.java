package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.MessageConfiguration;
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
            sender.sendMessage(MessageConfiguration.component("banking_unique_private_account"));
            return;
        }

        if (name.contains(" ")) {
            sender.sendMessage(MessageConfiguration.component("banking_account_name_no_spaces"));
            return;
        }

        if (name.length() < 3 || name.length() > 24) {
            sender.sendMessage(MessageConfiguration.component("banking_account_name_length"));
            return;
        }

        if (bankingModule.getAccountByName(name) != null) {
            sender.sendMessage(MessageConfiguration.component("banking_account_name_exists"));
            return;
        }

        if (!namePattern.matcher(name).matches()) {
            sender.sendMessage(MessageConfiguration.component("banking_account_name_invalid"));
            return;
        }

        UUID accountId = UUID.randomUUID();
        dataModule.getAdapter().createBankAccount(accountId, type, 0L, name, false).whenComplete(((accountModel, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(MessageConfiguration.component("banking_account_creation_error"));
                OpenMinetopia.getInstance().getLogger().severe("Something went wrong while trying to create an account: " + throwable.getMessage());
            }

            // TODO: Replace <account_name> with the actual value
            sender.sendMessage(MessageConfiguration.component("banking_account_created"));
            bankingModule.getBankAccountModels().add(accountModel);
            accountModel.initSavingTask();
        }));

    }

}
