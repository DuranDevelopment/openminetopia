package nl.openminetopia.api.player.fitness.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.api.player.fitness.FitnessManager;
import nl.openminetopia.api.player.fitness.booster.FitnessBoosterManager;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;

import java.util.List;

@Getter
public class Fitness {

    private final MinetopiaPlayer minetopiaPlayer;

    private int totalFitness;
    private double drinkingPoints;
    private int healthPoints;
    private @Setter long lastDrinkingTime;

    private int fitnessGainedByHealth;
    private int fitnessGainedByDrinking;
    private int fitnessGainedByClimbing;
    private int fitnessGainedByWalking;
    private int fitnessGainedBySprinting;
    private int fitnessGainedBySwimming;
    private int fitnessGainedByFlying;

    private List<FitnessBooster> fitnessBoosters;

    public Fitness(MinetopiaPlayer minetopiaPlayer) {
        this.minetopiaPlayer = minetopiaPlayer;
    }

    public void load() {
        FitnessManager.getInstance().getTotalFitness(minetopiaPlayer).whenComplete((totalFitness, throwable) -> {
            if (totalFitness == null) {
                this.totalFitness = 0;
                return;
            }
            this.totalFitness = totalFitness;
        });
        FitnessManager.getInstance().getDrinkingPoints(minetopiaPlayer).whenComplete((drinkingPoints, throwable) -> {
            if (drinkingPoints == null) {
                this.drinkingPoints = 0;
                return;
            }
            this.drinkingPoints = drinkingPoints;
        });
        FitnessManager.getInstance().getFitnessGainedByDrinking(minetopiaPlayer).whenComplete((fitnessGainedByDrinking, throwable) -> {
            if (fitnessGainedByDrinking == null) {
                this.fitnessGainedByDrinking = 0;
                return;
            }
            this.fitnessGainedByDrinking = fitnessGainedByDrinking;
        });
        FitnessManager.getInstance().getFitnessGainedByHealth(minetopiaPlayer).whenComplete((fitnessGainedByHealth, throwable) -> {
            if (fitnessGainedByHealth == null) {
                this.fitnessGainedByHealth = 0;
                return;
            }
            this.fitnessGainedByHealth = fitnessGainedByHealth;
        });

        FitnessBoosterManager.getInstance().getFitnessBoosters(minetopiaPlayer).whenComplete((fitnessBoosters, throwable) -> {
            if (fitnessBoosters == null) {
                this.fitnessBoosters = List.of();
                return;
            }
            this.fitnessBoosters = fitnessBoosters;
        });

        minetopiaPlayer.setFitness(this);
    }

    public void setTotalFitness(int amount) {
        this.totalFitness = amount;
        FitnessManager.getInstance().setTotalFitness(minetopiaPlayer, amount);
    }

    public void setDrinkingPoints(double amount) {
        this.drinkingPoints = amount;
        FitnessManager.getInstance().setDrinkingPoints(minetopiaPlayer, amount);
    }

    public void setHealthPoints(int amount) {
        this.healthPoints = amount;
        FitnessManager.getInstance().setHealthPoints(minetopiaPlayer, amount);
    }

    /**
     * Set fitness gained
     */
    public void setFitnessGainedByHealth(int amount) {
        this.fitnessGainedByHealth = amount;
        FitnessManager.getInstance().setFitnessGainedByHealth(minetopiaPlayer, amount);
    }

    public void setFitnessGainedByDrinking(int amount) {
        this.fitnessGainedByDrinking = amount;
        FitnessManager.getInstance().setFitnessGainedByDrinking(minetopiaPlayer, amount);
    }

    public void setFitnessGainedByClimbing(int amount) {
        this.fitnessGainedByClimbing = amount;
        FitnessManager.getInstance().setFitnessGainedByClimbing(minetopiaPlayer, amount);
    }

    public void setFitnessGainedBySprinting(int amount) {
        this.fitnessGainedBySprinting = amount;
        FitnessManager.getInstance().setFitnessGainedBySprinting(minetopiaPlayer, amount);
    }

    public void setFitnessGainedByFlying(int amount) {
        this.fitnessGainedByFlying = amount;
        FitnessManager.getInstance().setFitnessGainedByFlying(minetopiaPlayer, amount);
    }

    public void setFitnessGainedBySwimming(int amount) {
        this.fitnessGainedBySwimming = amount;
        FitnessManager.getInstance().setFitnessGainedBySwimming(minetopiaPlayer, amount);
    }

    public void setFitnessGainedByWalking(int amount) {
        this.fitnessGainedByWalking = amount;
        FitnessManager.getInstance().setFitnessGainedByWalking(minetopiaPlayer, amount);
    }

    public void addFitnessBooster(FitnessBooster booster) {
        fitnessBoosters.add(booster);
        FitnessBoosterManager.getInstance().addFitnessBooster(minetopiaPlayer, booster);
    }

    public void removeFitnessBooster(FitnessBooster booster) {
        fitnessBoosters.remove(booster);
        FitnessBoosterManager.getInstance().removeFitnessBooster(minetopiaPlayer, booster);
    }
}
