package nl.openminetopia.modules.color.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.modules.color.enums.OwnableColorType;

@Getter
@Setter
public abstract class OwnableColor {

    private OwnableColorType type;
    private int id;
    private String color;
    private long expiresAt;

    public OwnableColor(OwnableColorType type, int id, String color, long expiresAt) {
        this.type = type;
        this.id = id;
        this.color = color;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
    }
}
