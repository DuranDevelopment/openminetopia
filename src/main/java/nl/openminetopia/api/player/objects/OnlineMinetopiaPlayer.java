package nl.openminetopia.api.player.objects;

import lombok.Getter;
import nl.openminetopia.api.player.PrefixManager;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.List;
import java.util.UUID;

@Getter
public class OnlineMinetopiaPlayer implements MinetopiaPlayer {

    private final UUID uuid;
    private final PlayerModel playerModel;
    private int level;
    private List<Prefix> prefixes;
    private Prefix activePrefix;

    public OnlineMinetopiaPlayer(UUID uuid, PlayerModel playerModel) {
        this.uuid = uuid;
        this.playerModel = playerModel;
    }

    public void load() {
        this.level = playerModel.getLevel();
        this.activePrefix = PrefixManager.getInstance().getPlayerActivePrefix(this).join();
        this.prefixes = PrefixManager.getInstance().getPrefixes(this).join();
    }

    @Override
    public void setLevel(int level) {
        this.level = level;

        // Update the player model
        playerModel.setLevel(level);
        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    @Override
    public void addPrefix(Prefix prefix) {
        prefixes.add(prefix);
        PrefixManager.getInstance().addPrefix(this, prefix.getPrefix(), prefix.getExpiresAt());
    }

    @Override
    public void setActivePrefix(Prefix prefix) {
        this.activePrefix = prefix;

        // Update the player model
        playerModel.setActivePrefixId(prefix.getId());
        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    public Prefix getActivePrefix() {
        if (activePrefix == null) {
            activePrefix = new Prefix(-1, "Zwerver", -1);
        }

        return activePrefix;
    }
}