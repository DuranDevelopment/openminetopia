package nl.openminetopia.modules.data.adapter.utils;

import nl.openminetopia.modules.data.adapter.DatabaseAdapter;
import nl.openminetopia.modules.data.adapter.MongoAdapter;
import nl.openminetopia.modules.data.adapter.MySQLAdapter;
import nl.openminetopia.modules.data.adapter.SQLiteAdapter;
import nl.openminetopia.modules.data.type.DatabaseType;

public class AdapterUtil {

    public static DatabaseAdapter getAdapter(DatabaseType type) {
        return switch (type) {
            case MYSQL -> new MySQLAdapter();
            case SQLITE -> new SQLiteAdapter();
            case MONGO -> new MongoAdapter();
            default -> throw new IllegalArgumentException("Unsupported database type: " + type);
        };
    }
}
