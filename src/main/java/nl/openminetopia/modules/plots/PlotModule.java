package nl.openminetopia.modules.plots;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.plots.commands.*;
import nl.openminetopia.modules.plots.commands.subcommands.*;
import nl.openminetopia.utils.WorldGuardUtils;

public class PlotModule extends Module {

    public static StateFlag PLOT_FLAG = new StateFlag("openmt-plot", true);
    public static StringFlag PLOT_DESCRIPTION = new StringFlag("openmt-description");

    @Override
    public void enable() {
        registerCommand(new PlotInfoCommand());

        registerCommand(new PlotCommand());
        registerCommand(new PlotMembersCommand());
        registerCommand(new PlotOwnersCommand());
        registerCommand(new PlotClearCommand());
        registerCommand(new PlotCreateCommand());
        registerCommand(new PlotDeleteCommand());
        registerCommand(new PlotDescriptionCommand());
        registerCommand(new PlotListCommand());
        registerCommand(new PlotTeleportCommand());

        OpenMinetopia.getCommandManager().getCommandCompletions().registerCompletion("plotName", context ->
                WorldGuardUtils.getProtectedRegions(priority -> priority >= 0).stream()
                        .filter(region -> region.getFlag(PLOT_FLAG) != null)
                        .map(ProtectedRegion::getId).toList());

        loadFlags();
    }

    @Override
    public void disable() {

    }

    public void loadFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(PLOT_FLAG);
            registry.register(PLOT_DESCRIPTION);
        } catch (FlagConflictException e) {
            PLOT_FLAG = (StateFlag) registry.get("openmt-plot");
            PLOT_DESCRIPTION = (StringFlag) registry.get("openmt-description");
        }
    }
}
