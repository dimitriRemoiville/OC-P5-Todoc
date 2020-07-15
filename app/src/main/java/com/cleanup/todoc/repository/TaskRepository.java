package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.db.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskRepository {

    private final TaskDao mTaskDao;

    public TaskRepository(TaskDao taskDao) {
        this.mTaskDao = taskDao;
    }

    // Get task
    public LiveData<List<Task>> getTasks(long projectId) {
        return mTaskDao.getTasks(projectId);
    }

    // Get all tasks from any projects
    public LiveData<List<Task>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    // Create task
    public void createTask(Task task) {
        mTaskDao.insertTask(task);
    }

    // Delete task
    public void deleteTask(long taskId) {
        mTaskDao.deleteTask(taskId);
    }

    // Update task
    public void updateTask(Task task) {
        mTaskDao.updateTask(task);
    }
}
