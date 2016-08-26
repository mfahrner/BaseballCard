package com.theironyard.charlotte;


/**
 * Created by mfahrner on 8/25/16.
 */
public class Card {
    String name;
    int year;
    String type;
    String condition;


    public Card(String name, int year, String type, String condition) {
        this.name = name;
        this.year = year;
        this.type = type;
        this.condition = condition;
    }
}
