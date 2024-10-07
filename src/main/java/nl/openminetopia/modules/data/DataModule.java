package nl.openminetopia.modules.data;

import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.data.adapters.DatabaseAdapter;
import nl.openminetopia.modules.data.adapters.utils.AdapterUtil;
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
    }

    @Override
    public void disable() {
        if (adapter != null) {
            adapter.disconnect();
        }
    }

}
