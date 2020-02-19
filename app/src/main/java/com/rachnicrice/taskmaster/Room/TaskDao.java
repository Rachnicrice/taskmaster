package com.rachnicrice.taskmaster.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rachnicrice.taskmaster.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Insert
    void addTask(Task t);
}
