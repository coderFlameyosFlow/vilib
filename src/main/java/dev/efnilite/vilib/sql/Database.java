package dev.efnilite.vilib.sql;

import dev.efnilite.vilib.ViMain;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Database {

    private Connection connection;
    private String database;

    public void connect(String url, int port, String database, String username, String password) {
        this.database = database;

        try {
            ViMain.logging().info("Connecting to SQL...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // For newer versions
            } catch (ClassNotFoundException ignored) {
                Class.forName("com.mysql.jdbc.Driver"); // For older versions
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database
                    + "?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf-8"
                    + "&autoReconnect=true", username, password);

            initTables();
            ViMain.logging().info("Connected to SQL!");
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            ViMain.logging().error("Error while trying to connect to SQL!");
        }
    }

    protected abstract void initTables();

    public void query(String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            ViMain.logging().error("Error while trying to update MySQL database!");
            ViMain.logging().error("Query: " + query);
        }
    }

    public void suppressedQuery(String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.executeUpdate();
        } catch (SQLException ignored) { }
    }

    public @Nullable PreparedStatement resultQuery(String query) {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            ViMain.logging().error("Error while trying to fetch from MySQL database!");
            ViMain.logging().error("Query: " + query);
            return null;
        }
    }

    public void close() {
        try {
            connection.close();
            ViMain.logging().info("Closed connection to MySQL");
        } catch (SQLException ex) {
            ex.printStackTrace();
            ViMain.logging().error("Error while trying to close connection to MySQL database!");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}