package com.rachnicrice.taskmaster.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rachnicrice.taskmaster.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
