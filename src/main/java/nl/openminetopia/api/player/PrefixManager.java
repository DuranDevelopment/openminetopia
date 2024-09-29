package nl.openminetopia.api.player;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PrefixModel;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PrefixManager {

    private static PrefixManager instance;

    public static PrefixManager getInstance() {
        if (instance == null) {
            instance = new PrefixManager();
        }
        return instance;
    }

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    /*
     * Prefixes
     */

    public void addPrefix(MinetopiaPlayer player, Prefix prefix) {
        dataModule.getAdapter().addPrefix(player, prefix).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        });
    }

    public void setActivePrefix(MinetopiaPlayer player, Prefix prefix) {
        dataModule.getAdapter().setActivePrefix(player, prefix);
    }

    public void removePrefix(MinetopiaPlayer player, Prefix prefix) {
        dataModule.getAdapter().removePrefix(player, prefix);
    }

    public CompletableFuture<Prefix> getPlayerActivePrefix(MinetopiaPlayer player) {
        return dataModule.getAdapter().getActivePrefix(player);
    }

    public CompletableFuture<List<Prefix>> getPrefixes(MinetopiaPlayer player) {
        return dataModule.getAdapter().getPrefixes(player);
    }
}
