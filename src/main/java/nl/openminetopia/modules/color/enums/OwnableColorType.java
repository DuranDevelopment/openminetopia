package nl.openminetopia.modules.color.enums;

import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.color.objects.*;

@Getter
public enum OwnableColorType {
    PREFIX("<red>Prefixkleur", OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor()),
    CHAT("<green>Chatkleur", OpenMinetopia.getDefaultConfiguration().getDefaultChatColor()),
    NAME("<blue>Naamkleur", OpenMinetopia.getDefaultConfiguration().getDefaultNameColor()),
    LEVEL("<light_purple>Levelkleur", OpenMinetopia.getDefaultConfiguration().getDefaultLevelColor());

    private final String displayName;
    private final String defaultColor;

    OwnableColorType(String displayName, String defaultColor) {
        this.displayName = displayName;
        this.defaultColor = defaultColor;
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