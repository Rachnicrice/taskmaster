package com.rachnicrice.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.rachnicrice.taskmaster.Task;

import java.util.List;

@Entity
public class Team {

    @PrimaryKey
    public Long id;

    @ColumnInfo
    public final String team;

    @ColumnInfo
    public List<Task> tasks;

    //constructor function
    public Team (String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
