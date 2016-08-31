package com.theironyard.charlotte;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Created by mfahrner on 8/30/16.
 */
public class UserTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cards (id IDENTITY, name VARCHAR, year INT, type VARCHAR," +
                " condition VARCHAR, user_id INT)");
        return conn;
    }
    @Test
    public void insertUserANDselectUserCreatesUserSelectsCreatedUserReturnsUserNotNull() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "");
        User user = User.selectUser(conn, "mike");
        conn.close();
        assertTrue(user != null);
    }
}