package com.theironyard.charlotte;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by mfahrner on 8/25/16.
 */
public class Card {
    int id;
    String cardName;
    int year;
    String type;
    String condition;
    int user_id;

    public Card(int id, String cardName, int year, String type, String condition, int user_id) {
        this.id = id;
        this.cardName = cardName;
        this.year = year;
        this.type = type;
        this.condition = condition;
        this.user_id = user_id;
    }

    public String getName() {
        return cardName;
    }

    public void setName(String name) {
        this.cardName = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void insertCard(Connection conn, String name, int year, String type, String condition, int user_id)
            throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO cards VALUES (NULL, ?, ?, ?, ?, ?)");

        stmt.setString(1, name);
        stmt.setInt(2, year);
        stmt.setString(3, type);
        stmt.setString(4, condition);
        stmt.setInt(5, user_id);

        stmt.execute();
    }

    public static Card selectCard(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cards WHERE id = ?");

        stmt.setInt(1, id);

        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            String cardName = results.getString("name");
            int year = results.getInt("year");
            String type = results.getString("type");
            String condition = results.getString("condition");
            int user_id = results.getInt("user_id");

            return new Card(id, cardName, year, type, condition, user_id);
        }
        return null;
    }

    public static ArrayList<Card> searchCards(Connection conn, int id) throws SQLException {
        ArrayList<Card> cardList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users INNER JOIN cards ON " +
                "users.id = cards.user_id WHERE users.id = ?");

        stmt.setInt(1, id);

        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            int cardId = results.getInt("cards.id");
            String cardName = results.getString("cards.name");
            int year = results.getInt("year");
            String type = results.getString("type");
            String condition = results.getString("condition");
            int user_id = results.getInt("user_id");

            cardList.add(new Card(cardId, cardName, year, type, condition, user_id));
        }
        return cardList;
    }

    public static void updateCard(Connection conn, String cardName, int year, String type, String condition, int id)
            throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("UPDATE cards SET name = ?, year = ?, type = ?, condition = ?" +
                "WHERE id = ?");

        stmt.setString(1, cardName);
        stmt.setInt(2, year);
        stmt.setString(3, type);
        stmt.setString(4, condition);
        stmt.setInt(5, id);

        stmt.execute();
    }

    public static void deleteCard(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM cards WHERE id = ?");

        stmt.setInt(1, id);

        stmt.execute();
    }
}
