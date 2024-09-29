package nl.openminetopia.modules.player.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.adapters.utils.AdapterUtil;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        PlayerManager.getInstance().getPlayerModels().remove(event.getUniqueId());
        PlayerProfile player = event.getPlayerProfile();

        try {
            DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
            dataModule.getAdapter().loadPlayer(event.getUniqueId());

            BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
            bankingModule.getAccountModel(event.getUniqueId()).whenComplete(((bankAccountModel, throwable) -> {
                if(throwable != null) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er ging iets fout tijdens het ophalen van je bank gegevens."));
                    return;
                }

                if(bankAccountModel == null) {
                    BankAccountModel accountModel = new BankAccountModel();
                    accountModel.setBalance(0L); // todo: fix ooit startersbedrag.
                    accountModel.setName(event.getName());
                    accountModel.getUsers().put(event.getUniqueId(), AccountPermission.ADMIN);
                    bankingModule.getBankAccountModels().add(accountModel);
                    try {
                        bankingModule.createAccount(accountModel);
                        OpenMinetopia.getInstance().getLogger().info("Created account for: " + player.getName());
                    } catch (SQLException e) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er ging iets fout tijdens het ophalen van je bank gegevens."));
                        return;
                    }
                    return;
                }

                bankAccountModel.getUsers().put(player.getId(), AccountPermission.ADMIN);
                OpenMinetopia.getInstance().getLogger().info("Loaded account for: " + player.getName());
                bankingModule.getBankAccountModels().add(bankAccountModel);
            }));

        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
            OpenMinetopia.getInstance().getLogger().warning("Error loading player model: " + e.getMessage());
        }
    }
}
