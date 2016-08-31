package com.theironyard.charlotte;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cards (id IDENTITY, name VARCHAR, year INT, type VARCHAR," +
                " condition VARCHAR, user_id INT)");

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();

                    String name = session.attribute("name");

                    User user = User.selectUser(conn, name);

                    HashMap m = new HashMap();

                    if (user == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        m.put("name", user.name);
                        m.put("cardList", Card.searchCards(conn, user.id));
                        return new ModelAndView(m, "user.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("name");

                    String password = request.queryParams("password");

                    User user = User.selectUser(conn, name);

                    if (user != null && !user.password.equals(password)) {
                        Session session = request.session();
                        session.invalidate();

                        response.redirect("/");
                    }

                    if (user == null) {
                        User.insertUser(conn, name, password);
                    }

                    Session session = request.session();
                    session.attribute("name", name);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-card",
                ((request, response) -> {
                    Session session = request.session();

                    String name = session.attribute("name");

                    User user = User.selectUser(conn, name);

                    if (user == null) {
                        throw new Exception("User is not logged in");
                    }

                    int user_id = user.id;
                    String cardName = request.queryParams("cardName");
                    int year = Integer.valueOf(request.queryParams("year"));
                    String type = request.queryParams("type");
                    String condition = request.queryParams("condition");

                    Card.insertCard(conn, cardName, year, type, condition, user_id);

                    response.redirect("/");

                    return "";
                })
        );

        Spark.post(
                "/update-card/:id",
                ((request, response) -> {
                    Session session = request.session();

                    String name = session.attribute("name");

                    User user = User.selectUser(conn, name);

                    if (user == null) {
                        throw new Exception("User is not logged in");
                    }

                    String idValue = request.params("id");
                    int indexNumber = Integer.valueOf(idValue);

                    String cardName = request.queryParams("cardName");
                    int year = Integer.valueOf(request.queryParams("year"));
                    String type = request.queryParams("type");
                    String condition = request.queryParams("condition");

                    Card.updateCard(conn, cardName, year, type, condition, indexNumber);

                    response.redirect("/");

                    return "";
                })
        );

        Spark.get(
                "/update-card/:id",
                ((request, response) -> {
                    Session session = request.session();

                    String name = session.attribute("name");

                    String idValue = request.params("id");
                    int indexNumber = Integer.valueOf(idValue);

                    User user = User.selectUser(conn, name);

                    HashMap m = new HashMap();
                        m.put("id", indexNumber);
                        m.put("name", user.name);
                        return new ModelAndView(m, "edit.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();

                    response.redirect("/");

                    return "";
                })
        );

        Spark.post(
                "/delete/:id",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("name");

                    String idValue = request.params("id");
                    int removeNumber = Integer.valueOf(idValue);
//                    User user = User.selectUser(conn, name);

                    Card.deleteCard(conn, removeNumber);

                    response.redirect("/");

                    return "";
                })
        );
    }
}
