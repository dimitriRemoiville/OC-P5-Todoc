package com.cleanup.todoc.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    // Repositories
    private final TaskRepository mTaskDataSource;
    private final ProjectRepository mProjectDataSource;
    private final Executor mExecutor;
    private static final String TAG = "TaskViewModel";

    public TaskViewModel(TaskRepository taskDataSource, ProjectRepository projectDataSource, Executor executor) {
        super();
        this.mTaskDataSource = taskDataSource;
        this.mProjectDataSource = projectDataSource;
        this.mExecutor = executor;
    }

    // For Project
    public Project getProject(long projectId) {
        return mProjectDataSource.getProject(projectId);
    }

    public LiveData<List<Project>> getAllProject() {
        return mProjectDataSource.getAllProject();
    }

    // For task

    public LiveData<List<Task>> getTasks(long projectId) {
        return mTaskDataSource.getTasks(projectId);
    }

    public LiveData<List<Task>> getAllTasks() {
        return mTaskDataSource.getAllTasks();
    }

    public void createTask(Task task) {
        mExecutor.execute(() -> {
            mTaskDataSource.createTask(task);
        });
    }

    public void deleteTask(long taskId) {
        mExecutor.execute(() -> {
            mTaskDataSource.deleteTask(taskId);
            Log.d(TAG, "deleteTask: test");
        });
    }

    public void updateTask(Task task) {
        mExecutor.execute(() -> {
            mTaskDataSource.updateTask(task);
        });
    }

    public void clearTable() {
        mExecutor.execute(() -> {
            mTaskDataSource.clearTable();
        });
    }

}
