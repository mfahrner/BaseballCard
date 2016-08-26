package com.theironyard.charlotte;

import java.util.ArrayList;

/**
 * Created by mfahrner on 8/25/16.
 */
public class User {
    String name;
    String password;

    ArrayList<Card> cardList = new ArrayList<>();


    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
