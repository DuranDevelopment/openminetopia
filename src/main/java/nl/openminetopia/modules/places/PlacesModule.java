package nl.openminetopia.modules.places;

import nl.openminetopia.api.world.MTCityManager;
import nl.openminetopia.api.world.MTWorldManager;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.places.commands.mtcity.MTCityCommand;
import nl.openminetopia.modules.places.commands.mtworld.MTWorldCommand;
import nl.openminetopia.modules.places.commands.mtworld.subcommands.MTWorldCreate;

public class PlacesModule extends Module {

    @Override
    public void enable() {
        MTWorldManager.getInstance().loadWorlds();
        MTCityManager.getInstance().loadCities();

        registerCommand(new MTWorldCommand());
        registerCommand(new MTWorldCreate());

        registerCommand(new MTCityCommand());
    }

    @Override
    public void disable() {

    }
}
