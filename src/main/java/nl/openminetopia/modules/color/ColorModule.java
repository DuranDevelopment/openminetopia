package nl.openminetopia.modules.color;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.color.commands.ColorCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorAddCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorCreateCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorRemoveCommand;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;

import java.util.Arrays;
import java.util.List;

public class ColorModule extends Module {

    @Override
    public void enable() {
        registerCommand(new ColorCommand());
        registerCommand(new ColorAddCommand());
        registerCommand(new ColorRemoveCommand());
        registerCommand(new ColorCreateCommand());

        OpenMinetopia.getCommandManager().getCommandCompletions().registerCompletion("colorTypes", context ->
                Arrays.stream(OwnableColorType.values()).map(OwnableColorType::name).toList());

        OpenMinetopia.getCommandManager().getCommandCompletions().registerCompletion("playerColors", context -> {
            var player = PlayerManager.getInstance().getMinetopiaPlayer(context.getPlayer());
            if (player == null) return List.of();
            return player.getColors().stream().map(OwnableColor::getColorId).toList();
        });
    }

    @Override
    public void disable() {

    }
}
