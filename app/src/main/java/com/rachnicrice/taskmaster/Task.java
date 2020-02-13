package com.rachnicrice.taskmaster;

public class Task {
    //instance variables
    public final String title;
    public final String details;
    public String state;

    //constructor function
    public Task (String title, String details) {
        this.title = title;
        this.details = details;
        this.state = "new";
    }

    //instance methods
    @Override
    public String toString() {
        return title;
    }

    public void setState(String state) {
        this.state = state;
    }
}
