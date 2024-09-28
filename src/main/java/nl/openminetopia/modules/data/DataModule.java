package nl.openminetopia.modules.data;

import com.craftmend.storm.Storm;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.data.adapters.DatabaseAdapter;
import nl.openminetopia.modules.data.adapters.utils.AdapterUtil;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.*;
import nl.openminetopia.modules.data.types.DatabaseType;

@Getter
public class DataModule extends Module {

    private DatabaseAdapter adapter;

    @Override
    public void enable() {
        // TODO: Get this value from the config
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();
        DatabaseType type = configuration.getDatabaseType();

        adapter = AdapterUtil.getAdapter(type);
        adapter.connect();

        Storm storm = StormDatabase.getInstance().getStorm();

        if (type != DatabaseType.MONGO) {
            try {
                storm.registerModel(new PlayerModel());
                storm.runMigrations();
                storm.registerModel(new PrefixModel());
                storm.runMigrations();
                storm.registerModel(new ColorModel());
                storm.runMigrations();
                storm.registerModel(new FitnessModel());
                storm.runMigrations();
                storm.registerModel(new FitnessBoosterModel());
                storm.runMigrations();
                storm.registerModel(new WorldModel());
                storm.runMigrations();
                storm.registerModel(new CityModel());
                storm.runMigrations();
            } catch (Exception e) {
                OpenMinetopia.getInstance().getLogger().severe("Failed to connect to " + type.name() + " database: " + e.getMessage());
            }
        }
    }

    @Override
    public void disable() {
        if (adapter != null) {
            adapter.disconnect();
        }
    }
}
