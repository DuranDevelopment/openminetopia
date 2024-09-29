package nl.openminetopia.modules.data;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.parser.types.TypeRegistry;
import lombok.Getter;
import lombok.SneakyThrows;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.adapters.DatabaseAdapter;
import nl.openminetopia.modules.data.adapters.utils.AdapterUtil;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.adapters.AccountPermissionAdapter;
import nl.openminetopia.modules.data.storm.adapters.AccountTypeAdapter;
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
                TypeRegistry.registerAdapter(AccountType.class, new AccountTypeAdapter());
                TypeRegistry.registerAdapter(AccountPermission.class, new AccountPermissionAdapter());

                registerStormModel(new BankAccountModel());
                registerStormModel(new BankPermissionModel());
                registerStormModel(new PlayerModel());
                registerStormModel(new PrefixModel());
                registerStormModel(new ColorModel());
                registerStormModel(new FitnessModel());
                registerStormModel(new FitnessBoosterModel());
                registerStormModel(new WorldModel());
                registerStormModel(new CityModel());
                storm.runMigrations();
            } catch (Exception e) {
                OpenMinetopia.getInstance().getLogger().severe("Failed to connect to " + type + " database: " + e.getMessage());
                OpenMinetopia.getInstance().getLogger().severe("Disabling the plugin...");
                OpenMinetopia.getInstance().getServer().getPluginManager().disablePlugin(OpenMinetopia.getInstance());
            }
        }
    }

    @Override
    public void disable() {
        if (adapter != null) {
            adapter.disconnect();
        }
    }

    @SneakyThrows
    private void registerStormModel(StormModel model) {
        Storm storm = StormDatabase.getInstance().getStorm();
        storm.registerModel(model);
        storm.runMigrations();
    }
}
