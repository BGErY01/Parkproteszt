package hu.parkolo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:sqlite:database/parkolo.db";

    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        // Idegen kulcsok ellenőrzésének bekapcsolása
        connection.createStatement().execute("PRAGMA foreign_keys = ON");

        return connection;
    }
}
