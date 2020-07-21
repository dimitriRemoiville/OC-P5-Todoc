package com.cleanup.todoc;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.db.TodocDatabase;
import com.cleanup.todoc.db.dao.ProjectDao;
import com.cleanup.todoc.db.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertThat;

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(JUnit4.class)
public class DbUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;
    private TodocDatabase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        mDb = Room.inMemoryDatabaseBuilder(context, TodocDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        mTaskDao = mDb.mTaskDao();
        mProjectDao = mDb.mProjectDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertAndGetTask() throws Exception {
        Task task = new Task(1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertTrue(allTasks.contains(task));
    }

    @Test
    public void getAllTasks() throws Exception {
        Task task = new Task(1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task);
        Task task2 = new Task(2, "task 2", new Date().getTime());
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "task 3", new Date().getTime());
        mTaskDao.insertTask(task3);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertThat(allTasks, IsIterableContainingInAnyOrder.containsInAnyOrder(Arrays.asList(task,task2,task3)));
    }

    @Test
    public void deleteAll() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }
}