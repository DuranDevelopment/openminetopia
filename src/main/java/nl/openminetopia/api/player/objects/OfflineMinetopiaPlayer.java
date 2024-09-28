package nl.openminetopia.api.player.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.api.player.fitness.FitnessManager;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

@Getter
public class OfflineMinetopiaPlayer implements MinetopiaPlayer {
    private final UUID uuid;

    private @Setter Fitness fitness;

    public OfflineMinetopiaPlayer(UUID uuid) {
        this.uuid = uuid;

        this.fitness = FitnessManager.getInstance().getFitness(this);
    }

    @Override
    public PlayerModel getPlayerModel() {
        return null;
    }

    @Override
    public OfflinePlayer getBukkit() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Override
    public void setPlaytime(int playtime, boolean updateDatabase) {

    }

    /**
     * Returns the playtime in seconds
     */
    @Override
    public int getPlaytime() {
        return 0;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public List<Prefix> getPrefixes() {
        return List.of();
    }

    @Override
    public void addPrefix(Prefix prefix) {

    }

    @Override
    public void removePrefix(Prefix prefix) {

    }

    @Override
    public Prefix getActivePrefix() {
        return null;
    }

    @Override
    public void setActivePrefix(Prefix prefix) {

    }

    @Override
    public List<OwnableColor> getColors() {
        return List.of();
    }

    @Override
    public void setActiveColor(OwnableColor color, OwnableColorType type) {

    }

    @Override
    public OwnableColor getActiveColor(OwnableColorType type) {
        return null;
    }

    @Override
    public void addColor(OwnableColor color) {

    }

    @Override
    public void removeColor(OwnableColor color) {

    }

    @Override
    public boolean isInPlace() {
        return false;
    }

    @Override
    public MTPlace getPlace() {
        return null;
    }

    @Override
    public MTWorld getWorld() {
        return null;
    }


}
