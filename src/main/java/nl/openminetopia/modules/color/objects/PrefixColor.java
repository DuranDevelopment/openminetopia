package nl.openminetopia.modules.color.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.modules.color.enums.OwnableColorType;

@Getter
@Setter
public class PrefixColor extends OwnableColor {

    public PrefixColor(int id, String color, long expiresAt) {
        super(OwnableColorType.PREFIX, id, color, expiresAt);
        this.id = id;
        this.color = color;
        this.expiresAt = expiresAt;
    }

    public PrefixColor(String color, long expiresAt) {
        super(OwnableColorType.PREFIX, color, expiresAt);
        this.color = color;
        this.expiresAt = expiresAt;
    }

}
