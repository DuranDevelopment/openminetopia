package nl.openminetopia.modules.color.enums;

import lombok.Getter;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.color.objects.*;

@Getter
public enum OwnableColorType {
    PREFIX(MessageConfiguration.message("color_prefix_display_name")),
    CHAT(MessageConfiguration.message("color_chat_display_name")),
    NAME(MessageConfiguration.message("color_name_display_name")),
    LEVEL(MessageConfiguration.message("color_level_display_name")),;

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