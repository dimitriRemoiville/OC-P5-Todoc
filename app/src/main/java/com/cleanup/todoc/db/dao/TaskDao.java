package com.cleanup.todoc.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM TASK_TABLE WHERE projectId = :projectId")
    LiveData<List<Task>> getTasks(long projectId);

    @Query("SELECT * FROM TASK_TABLE")
    LiveData<List<Task>> getAllTasks();

    @Insert
    long insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("DELETE FROM TASK_TABLE WHERE id = :taskId")
    int deleteTask(long taskId);
}
