package nl.openminetopia.api.player.fitness.booster.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FitnessBooster {

    private int id;
    private int amount;
    private long expiresAt;

    public FitnessBooster(int amount, long expiresAt) {
        this.amount = amount;
        this.expiresAt = expiresAt;
    }

    public FitnessBooster(int id, int amount, long expiresAt) {
        this.id = id;
        this.amount = amount;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() >= expiresAt;
    }
}
