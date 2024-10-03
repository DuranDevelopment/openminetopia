package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("color")
public class ColorCreateCommand extends BaseCommand {

    @Subcommand("create")
    @Syntax("<identifier> <display_name> <prefix_color>")
    @CommandPermission("openminetopia.color.create")
    @Description("Add a new color to the configuration.")
    public void create(Player player, String identifier, String displayName, String prefixColor) {
        OpenMinetopia.getColorsConfiguration().createColor(identifier, displayName, prefixColor);
        player.sendMessage(ChatUtils.color("<gold>Succesvol toegevoegd, kleur: " + prefixColor + identifier));
    }

}
