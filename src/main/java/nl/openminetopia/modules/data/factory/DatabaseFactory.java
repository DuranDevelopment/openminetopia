package nl.openminetopia.modules.data.factory;

import nl.openminetopia.modules.data.adapter.DatabaseAdapter;
import nl.openminetopia.modules.data.adapter.MongoAdapter;
import nl.openminetopia.modules.data.adapter.MySQLAdapter;
import nl.openminetopia.modules.data.type.DatabaseType;

public class DatabaseFactory {

    public static DatabaseAdapter getAdapter(DatabaseType type) {
        return switch (type) {
            case MYSQL -> new MySQLAdapter();
            case MONGO -> new MongoAdapter();
            default -> throw new IllegalArgumentException("Unsupported database type: " + type);
        };
    }
}
