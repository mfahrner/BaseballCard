package com.theironyard.charlotte;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap <String, User> users = new HashMap<>();

    public static void main(String[] args) {
        // adding test user to pop hashmap to test functionality of login
        addTestUser();

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();

                    String name = session.attribute("name");

                    User user = users.get(name);

                    HashMap m = new HashMap();

                    if (user == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        m.put("name", user.name);
                        m.put("cardList", user.cardList);
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

                    User user = users.get(name);
                    if ((user != null) && (!users.get(name).password.equals(password))) {
                        Session session = request.session();
                        session.invalidate();

                        response.redirect("/");
                    }

                    if (user == null) {
                        user = new User(name, password);
                        users.put(name, user);
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

                    User user = users.get(name);
                    if (user == null) {
                        throw new Exception("User is not logged in");
                    }

                    String cardName = request.queryParams("cardName");
                    int year = Integer.valueOf(request.queryParams("year"));
                    String type = request.queryParams("type");
                    String condition = request.queryParams("condition");

                    Card userCard = new Card(cardName, year, type, condition);
                    user.cardList.add(userCard);

                    response.redirect("/");

                    return "";
                })
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

    }

    static void addTestUser() {
        users.put("mike", new User("mike", "1234"));
    }
}
