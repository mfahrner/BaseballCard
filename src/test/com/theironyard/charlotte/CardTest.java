package com.theironyard.charlotte;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by mfahrner on 8/30/16.
 */
public class CardTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cards (id IDENTITY, name VARCHAR, year INT, type VARCHAR," +
                " condition VARCHAR, user_id INT)");
        return conn;
    }

    @Test
    public void insertCardANDselectCardInsertCardSelectCardThatWasInsertedReturnsTrueIfCardIsNull() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "");
        User user = User.selectUser(conn, "mike");
        Card.insertCard(conn, "card", 1, "super", "mint", 1);
        Card card = Card.selectCard(conn, 1);
        conn.close();
        assertTrue(card != null);
    }

    @Test
    public void searchCardsANDdeleteCardCreatesUserInsertsThreeCardsDeletesOneCardPopulatesArrayListReturnsTrueIfSizeOfArrayListIsTwo() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "");
        User user = User.selectUser(conn, "mike");
        Card.insertCard(conn, "yamama", 2, "perfect", "mint", 1);
        Card.insertCard(conn, "butts", 2, "mint", "perfect", 1);
        Card.insertCard(conn, "deleter", 3, "deleted", "deleterest", 1);
        Card.deleteCard(conn, 3);
        ArrayList<Card> testaroo = Card.searchCards(conn, 1);
        conn.close();
        assertTrue(testaroo.size() == 2);
    }

    @Test
    public void updateCardInsertsUserInsertsCardUpdatesCardReturnsTrueIfUpdatedNameIsSameAsStringPassedIn() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "");
        User user = User.selectUser(conn, "mike");
        Card.insertCard(conn, "card", 1, "super", "mint", 1);
        Card.updateCard(conn, "butts", 1, "super", "mint", 1);
        Card card = Card.selectCard(conn, 1);
        conn.close();
        assertEquals(card.cardName, "butts");
    }
}