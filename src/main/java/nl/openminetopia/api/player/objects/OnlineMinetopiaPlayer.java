package nl.openminetopia.api.player.objects;

import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.FitnessManager;
import nl.openminetopia.api.player.LevelManager;
import nl.openminetopia.api.player.PrefixManager;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.fitness.runnables.FitnessRunnable;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
public class OnlineMinetopiaPlayer implements MinetopiaPlayer {

    private final UUID uuid;
    private final PlayerModel playerModel;

    private int level;
    private List<Prefix> prefixes;

    private int totalPoints;
    private int climbingPoints;
    private int walkingPoints;

    private Prefix activePrefix;
    private FitnessRunnable fitnessRunnable;

    public OnlineMinetopiaPlayer(UUID uuid, PlayerModel playerModel) {
        this.uuid = uuid;
        this.playerModel = playerModel;
    }

    public void load() {
        this.level = playerModel.getLevel();
        this.activePrefix = PrefixManager.getInstance().getPlayerActivePrefix(this).join();
        this.prefixes = PrefixManager.getInstance().getPrefixes(this).join();

        this.fitnessRunnable = new FitnessRunnable(getBukkit());
    }

    @Override
    public Player getBukkit() {
        return Bukkit.getPlayer(uuid);
    }

    /* Level */

    @Override
    public void setLevel(int level) {
        this.level = level;

        // Update the player model
        LevelManager.getInstance().setLevel(this, level);
//        playerModel.setLevel(level);
//        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    /* Prefix */

    @Override
    public void addPrefix(Prefix prefix) {
        prefixes.add(prefix);
        PrefixManager.getInstance().addPrefix(this, prefix.getPrefix(), prefix.getExpiresAt());
    }

    @Override
    public void removePrefix(Prefix prefix) {
        prefixes.remove(prefix);
        PrefixManager.getInstance().removePrefix(this, prefix);
    }

    @Override
    public void setActivePrefix(Prefix prefix) {
        this.activePrefix = prefix;

        // Update the player model
        PrefixManager.getInstance().setActivePrefixId(this, prefix.getId());
//        playerModel.setActivePrefixId(prefix.getId());
//        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    @Override
    public Prefix getActivePrefix() {
        if (activePrefix == null) {
            activePrefix = new Prefix(-1, "Zwerver", -1);
        }

        if (activePrefix.getExpiresAt() < System.currentTimeMillis() && activePrefix.getExpiresAt() != -1) {
            getBukkit().sendMessage(ChatUtils.color("<red>Je prefix " + activePrefix + " is verlopen!"));
            removePrefix(activePrefix);
            setActivePrefix(new Prefix(-1, "Zwerver", -1));
        }

        return activePrefix;
    }

    /* Fitness */

    @Override
    public void setTotalPoints(int points) {
        this.totalPoints = points;
        FitnessManager.getInstance().setTotalPoints(this, points);
    }

    @Override
    public void setClimbingPoints(int points) {
        this.climbingPoints = points;
        FitnessManager.getInstance().setClimbingPoints(this, points);
    }

    @Override
    public void setWalkingPoints(int points) {
        this.walkingPoints = points;
        FitnessManager.getInstance().setWalkingPoints(this, points);
    }
}