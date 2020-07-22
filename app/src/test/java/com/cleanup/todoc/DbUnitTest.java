package com.cleanup.todoc;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.db.TodocDatabase;
import com.cleanup.todoc.db.dao.ProjectDao;
import com.cleanup.todoc.db.dao.TaskDao;
import com.cleanup.todoc.di.Injection;
import com.cleanup.todoc.di.ViewModelFactory;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;
import com.cleanup.todoc.ui.viewmodel.TaskViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.runners.JUnit4;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
public class DbUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TodocDatabase mDb;
    private TaskViewModel taskViewModel;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, TodocDatabase.class)
                .allowMainThreadQueries()
                .addCallback(populateDatabase())
                .build();
        ViewModelFactory viewModelFactory = new ViewModelFactory(
                new TaskRepository(mDb.mTaskDao()),
                new ProjectRepository(mDb.mProjectDao()),
                Executors.newSingleThreadExecutor());
        ViewModelStore viewModelStore = new ViewModelStore();
        taskViewModel = new ViewModelProvider(viewModelStore, viewModelFactory).get(TaskViewModel.class);
    }

    private static RoomDatabase.Callback populateDatabase() {
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1L);
                contentValues.put("name", "Projet Tartampion");
                contentValues.put("color", 0xFFEADAD1);
                db.insert("project_table", OnConflictStrategy.IGNORE, contentValues);
                contentValues.clear();
                contentValues.put("id", 2L);
                contentValues.put("name", "Projet Lucidia");
                contentValues.put("color", 0xFFB4CDBA);
                db.insert("project_table", OnConflictStrategy.IGNORE, contentValues);
                contentValues.clear();
                contentValues.put("id", 3L);
                contentValues.put("name", "Projet Circus");
                contentValues.put("color", 0xFFA3CED2);
                db.insert("project_table", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertAndGetTask() throws Exception {
        Task task = new Task(1, "task 1", new Date().getTime());
        taskViewModel.createTask(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(taskViewModel.getAllTasks());
        assertTrue(allTasks.contains(task));
    }

    @Test
    public void getAllTasks() throws Exception {
        Task task = new Task(1, "task 1", new Date().getTime());
        taskViewModel.createTask(task);
        Task task2 = new Task(2, "task 2", new Date().getTime());
        taskViewModel.createTask(task2);
        Task task3 = new Task(3, "task 3", new Date().getTime());
        taskViewModel.createTask(task3);
        List<Task> allTasks = LiveDataTestUtil.getValue(taskViewModel.getAllTasks());
        assertThat(allTasks, IsIterableContainingInAnyOrder.containsInAnyOrder(Arrays.asList(task, task2, task3)));
    }

    @Test
    public void deleteAll() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void test_projects() {

    }

    @Test
    public void test_az_comparator() {

    }

    @Test
    public void test_za_comparator() {

    }

    @Test
    public void test_recent_comparator() {

    }

    @Test
    public void test_old_comparator() {

    }
}