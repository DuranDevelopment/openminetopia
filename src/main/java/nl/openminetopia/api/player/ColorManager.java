package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.ColorModel;
import nl.openminetopia.modules.data.storm.models.PlayerModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColorManager {

    private static ColorManager instance;

    public static ColorManager getInstance() {
        if (instance == null) {
            instance = new ColorManager();
        }
        return instance;
    }

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    /**
     * Prefix colors
     */

    public void addColor(MinetopiaPlayer player, OwnableColor color) {
        dataModule.getAdapter().addColor(player, color);
    }

    public void removeColor(MinetopiaPlayer player, OwnableColor color) {
        dataModule.getAdapter().removeColor(player, color);
    }

    public void setActiveColor(MinetopiaPlayer player, OwnableColor color) {
        dataModule.getAdapter().setActiveColor(player, color, color.getType());
    }

    public CompletableFuture<List<OwnableColor>> getColors(MinetopiaPlayer player) {
        return dataModule.getAdapter().getColors(player);
    }

    public CompletableFuture<OwnableColor> getPlayerActiveColor(MinetopiaPlayer player, OwnableColorType type) {
        return dataModule.getAdapter().getActiveColor(player, type);
    }
}
