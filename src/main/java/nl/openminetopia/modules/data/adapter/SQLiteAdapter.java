package nl.openminetopia.modules.data.adapter;

import com.craftmend.storm.Storm;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.data.storm.StormDatabase;

import java.io.File;

public class SQLiteAdapter extends MySQLAdapter {

    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            StormDatabase.getInstance().setStorm(new Storm(new SqliteFileDriver(new File(OpenMinetopia.getInstance().getDataFolder(), "database.db"))));
        } catch (Exception e) {
            OpenMinetopia.getInstance().getLogger().severe("Failed to connect to SQLite database: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        // Disconnect from SQLite database
    }
}
