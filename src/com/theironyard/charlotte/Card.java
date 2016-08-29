package com.theironyard.charlotte;


/**
 * Created by mfahrner on 8/25/16.
 */
public class Card {
    String name;
    int year;
    String type;
    String condition;
    int id;
    static int cardsMade = 0;


    public Card(String name, int year, String type, String condition) {
        this.name = name;
        this.year = year;
        this.type = type;
        this.condition = condition;
        id = cardsMade++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
