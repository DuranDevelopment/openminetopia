package nl.openminetopia.modules.prefix.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrefixColor {

    private int id;
    private String color;
    private long expiresAt;

    public PrefixColor(int id, String color, long expiresAt) {
        this.id = id;
        this.color = color;
        this.expiresAt = expiresAt;
    }
}
