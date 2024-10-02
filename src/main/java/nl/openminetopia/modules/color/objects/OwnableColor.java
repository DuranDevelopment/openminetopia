package nl.openminetopia.modules.color.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.ColorsConfiguration;
import nl.openminetopia.configuration.components.ColorComponent;
import nl.openminetopia.modules.color.enums.OwnableColorType;

@Getter
@Setter
public abstract class OwnableColor {

    private OwnableColorType type;
    public int id;
    public String colorId;
    public long expiresAt;

    public OwnableColor(OwnableColorType type, int id, String colorId, long expiresAt) {
        this.type = type;
        this.id = id;
        this.colorId = colorId;
        this.expiresAt = expiresAt;
    }

    public OwnableColor(OwnableColorType type, String colorId, long expiresAt) {
        this.type = type;
        this.colorId = colorId;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
    }

    public String displayName() {
        ColorComponent component = config().color(colorId);
        if (component == null) component = new ColorComponent(null, "<gray>Default", "<gray>");

        return component.displayName();
    }

    public String color() {
        ColorComponent component = config().color(colorId);
        if (component == null) component = new ColorComponent(null, "<gray>Default", "<gray>");

        return component.colorPrefix();
    }

    private ColorsConfiguration config() {
        return OpenMinetopia.getColorsConfiguration();
    }

}
