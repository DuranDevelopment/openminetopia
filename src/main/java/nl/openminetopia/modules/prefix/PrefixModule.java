package nl.openminetopia.modules.prefix;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.color.objects.OwnableColor;
import nl.openminetopia.modules.prefix.commands.PrefixCommand;
import nl.openminetopia.modules.prefix.commands.subcommands.PrefixAddCommand;
import nl.openminetopia.modules.prefix.commands.subcommands.PrefixMenuCommand;
import nl.openminetopia.modules.prefix.commands.subcommands.PrefixRemoveCommand;
import nl.openminetopia.modules.prefix.objects.Prefix;

import java.util.List;

public class PrefixModule extends Module {
    @Override
    public void enable() {
        registerCommand(new PrefixCommand());
        registerCommand(new PrefixMenuCommand());
        registerCommand(new PrefixAddCommand());
        registerCommand(new PrefixRemoveCommand());

        OpenMinetopia.getCommandManager().getCommandCompletions().registerCompletion("playerPrefixes", context -> {
            var player = PlayerManager.getInstance().getMinetopiaPlayer(context.getPlayer());
            if (player == null) return List.of();
            return player.getPrefixes().stream().map(Prefix::getPrefix).toList();
        });
    }

    @Override
    public void disable() {

    }
}
