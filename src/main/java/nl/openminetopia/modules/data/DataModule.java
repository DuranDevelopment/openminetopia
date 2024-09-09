package nl.openminetopia.modules.data;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.data.adapter.DatabaseAdapter;
import nl.openminetopia.modules.data.factory.DatabaseFactory;
import nl.openminetopia.modules.data.type.DatabaseType;

public class DataModule extends Module {

    private DatabaseAdapter adapter;

    @Override
    public void enable() {
        // TODO: Get this value from the config
        DatabaseType type = DatabaseType.MYSQL;

        adapter = DatabaseFactory.getAdapter(type);
        adapter.connect();
    }

    @Override
    public void disable() {
        if (adapter != null) {
            adapter.disconnect();
        }
    }
}
