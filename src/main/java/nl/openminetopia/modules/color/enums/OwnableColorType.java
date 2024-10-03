package nl.openminetopia.modules.color.enums;

import lombok.Getter;
import nl.openminetopia.modules.color.objects.*;

@Getter
public enum OwnableColorType {
    PREFIX("<red>Prefixkleur"),
    CHAT("<green>Chatkleur"),
    NAME("<blue>Naamkleur"),
    LEVEL("<light_purple>Levelkleur"),;

    private final String displayName;

    OwnableColorType(String displayName) {
        this.displayName = displayName;
    }

    public Class<? extends OwnableColor> correspondingClass() {
        return switch (this) {
            case PREFIX -> PrefixColor.class;
            case CHAT -> ChatColor.class;
            case NAME -> NameColor.class;
            case LEVEL -> LevelColor.class;
        };
    }

}