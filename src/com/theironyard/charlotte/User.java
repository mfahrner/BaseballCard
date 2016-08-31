package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mfahrner on 8/25/16.
 */
public class User {
    int id;
    String name;
    String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static void insertUser(Connection conn, String name, String password ) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");

        stmt.setString(1, name);
        stmt.setString(2, password);

        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);

        ResultSet results = stmt.executeQuery();

        if (results.next()) {
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, name, password);
        }
        return null;
    }
}
