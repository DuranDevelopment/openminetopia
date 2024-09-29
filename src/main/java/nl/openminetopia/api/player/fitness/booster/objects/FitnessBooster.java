package nl.openminetopia.api.player.fitness.booster.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FitnessBooster {

    private int id;
    private int amount;
    private long expiresAt;

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() >= expiresAt;
    }
}
