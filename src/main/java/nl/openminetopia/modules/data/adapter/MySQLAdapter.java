package nl.openminetopia.modules.data.adapter;

import com.craftmend.storm.Storm;
import com.craftmend.storm.connection.hikaricp.HikariDriver;
import com.zaxxer.hikari.HikariConfig;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.data.storm.StormDatabase;

public class MySQLAdapter implements DatabaseAdapter {

    @Override
    public void connect() {
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();
        String host = configuration.getHost();
        int port = configuration.getPort();
        String name = configuration.getDatabaseName();
        String username = configuration.getUsername();
        String password = configuration.getPassword();

        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            StormDatabase.getInstance().setStorm(new Storm(new HikariDriver(config)));
        } catch (Exception e) {
            OpenMinetopia.getInstance().getLogger().severe("Failed to connect to MySQL database: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        // Disconnect from MySQL database
    }
}
