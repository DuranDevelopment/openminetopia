package nl.openminetopia.api.player.objects;

import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.fitness.objects.FitnessBooster;
import nl.openminetopia.modules.prefix.objects.Prefix;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class OfflineMinetopiaPlayer implements MinetopiaPlayer {


    public OfflineMinetopiaPlayer(UUID uuid) {
        super();
    }

    @Override
    public UUID getUuid() {
        return null;
    }

    @Override
    public PlayerModel getPlayerModel() {
        return null;
    }

    @Override
    public OfflinePlayer getBukkit() {
        return null;
    }

    @Override
    public boolean isScoreboardVisible() {
        return false;
    }

    @Override
    public void setScoreboardVisible(boolean visible) {

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
    public List<PrefixColor> getPrefixColors() {
        return List.of();
    }

    @Override
    public void addPrefixColor(PrefixColor color) {

    }

    @Override
    public void removePrefixColor(PrefixColor color) {

    }

    @Override
    public PrefixColor getActivePrefixColor() {
        return null;
    }

    @Override
    public void setActivePrefixColor(PrefixColor color) {

    }

    @Override
    public void setLastDrinkingTime(long time) {

    }

    @Override
    public long getLastDrinkingTime() {
        return 0;
    }

    @Override
    public void setFitness(int amount) {

    }

    @Override
    public int getFitness() {
        return 0;
    }

    @Override
    public void setHealthPoints(int points) {

    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public void setDrinkingPoints(double points) {

    }

    @Override
    public double getDrinkingPoints() {
        return 0;
    }

    @Override
    public void setFitnessGainedByHealth(int points) {

    }

    @Override
    public int getFitnessGainedByHealth() {
        return 0;
    }

    @Override
    public void setFitnessGainedByDrinking(int points) {

    }

    @Override
    public int getFitnessGainedByDrinking() {
        return 0;
    }

    @Override
    public void setFitnessGainedByClimbing(int points) {

    }

    @Override
    public int getFitnessGainedByClimbing() {
        return 0;
    }

    @Override
    public void setFitnessGainedByWalking(int points) {

    }

    @Override
    public int getFitnessGainedByWalking() {
        return 0;
    }

    @Override
    public void setFitnessGainedBySprinting(int points) {

    }

    @Override
    public int getFitnessGainedBySprinting() {
        return 0;
    }

    @Override
    public void setFitnessGainedBySwimming(int points) {

    }

    @Override
    public int getFitnessGainedBySwimming() {
        return 0;
    }

    @Override
    public void setFitnessGainedByFlying(int points) {

    }

    @Override
    public int getFitnessGainedByFlying() {
        return 0;
    }

    @Override
    public void addFitnessBooster(FitnessBooster booster) {

    }

    @Override
    public void removeFitnessBooster(FitnessBooster booster) {

    }

    @Override
    public List<FitnessBooster> getFitnessBoosters() {
        return List.of();
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
