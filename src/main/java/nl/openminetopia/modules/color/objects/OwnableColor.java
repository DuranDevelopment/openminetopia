package nl.openminetopia.modules.color.objects;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.ColorsConfiguration;
import nl.openminetopia.configuration.components.ColorComponent;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.utils.ChatUtils;

@Getter
@Setter
public abstract class OwnableColor {

    private OwnableColorType type;
    public int id;
    public String color;
    public long expiresAt;

    public OwnableColor(OwnableColorType type, int id, String color, long expiresAt) {
        this.type = type;
        this.id = id;
        this.color = color;
        this.expiresAt = expiresAt;
    }

    public OwnableColor(OwnableColorType type, String color, long expiresAt) {
        this.type = type;
        this.color = color;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
    }

    public String displayName() {
        ColorComponent component = config().color(color);
        if (component == null) component = new ColorComponent(null, "<gray>Default", "<gray>");

        return component.displayName();
    }

    public String color() {
        ColorComponent component = config().color(color);
        if (component == null) component = new ColorComponent(null, "<gray>Default", "<gray>");

        return component.colorPrefix();
    }

    private ColorsConfiguration config() {
        return OpenMinetopia.getColorsConfiguration();
    }

}
