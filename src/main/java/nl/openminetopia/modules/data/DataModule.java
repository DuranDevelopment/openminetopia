package nl.openminetopia.modules.data;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.types.TypeRegistry;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.data.adapter.DatabaseAdapter;
import nl.openminetopia.modules.data.adapter.utils.AdapterUtil;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.adapter.TimestampAdapter;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.data.storm.models.PrefixColorsModel;
import nl.openminetopia.modules.data.storm.models.PrefixesModel;
import nl.openminetopia.modules.data.type.DatabaseType;

import java.sql.Timestamp;

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
                TypeRegistry.registerAdapter(Timestamp.class, new TimestampAdapter());

                storm.registerModel(new PlayerModel());
                storm.registerModel(new PrefixesModel());
                storm.registerModel(new PrefixColorsModel());
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
