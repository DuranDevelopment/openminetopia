package nl.openminetopia.modules.color.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.modules.color.enums.OwnableColorType;

@Getter
@Setter
public class ChatColor extends OwnableColor {

    private int id;
    private String color;
    private long expiresAt;

    public ChatColor(int id, String color, long expiresAt) {
        super(OwnableColorType.CHAT, id, color, expiresAt);
        this.id = id;
        this.color = color;
        this.expiresAt = expiresAt;
    }
}
