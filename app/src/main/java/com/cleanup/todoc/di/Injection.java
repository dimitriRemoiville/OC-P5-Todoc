package com.cleanup.todoc.di;

import android.content.Context;

import com.cleanup.todoc.db.TodocDatabase;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static TaskRepository provideTaskRepository(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new TaskRepository(database.mTaskDao());
    }

    public static ProjectRepository provideProjectRepository(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new ProjectRepository(database.mProjectDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskRepository taskRepository = provideTaskRepository(context);
        ProjectRepository projectRepository = provideProjectRepository(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(taskRepository, projectRepository, executor);
    }
}
