package com.rachnicrice.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.rachnicrice.taskmaster.Team;

@Entity
public class Task {
    //instance variables
    @PrimaryKey
    public Long id;

    @ColumnInfo
    public final String title;

    @ColumnInfo
    public final String details;

    @ColumnInfo
    public String state;

    @ColumnInfo
    public Team team;

    //constructor function
    public Task (String title, String details, Team team) {
        this.title = title;
        this.details = details;
        this.state = "new";
        this.team = team;
    }

    //instance methods
    @Override
    public String toString() {
        return title;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getState() {
        return state;
    }
}
