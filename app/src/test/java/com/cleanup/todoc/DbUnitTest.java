package com.cleanup.todoc;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.db.TodocDatabase;
import com.cleanup.todoc.db.dao.ProjectDao;
import com.cleanup.todoc.db.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(RobolectricTestRunner.class)
public class DbUnitTest {


    private TodocDatabase mDb;
    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        mDb = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), TodocDatabase.class)
                .allowMainThreadQueries()
                .addCallback(populateDatabase())
                .build();
        mTaskDao = mDb.mTaskDao();
        mProjectDao = mDb.mProjectDao();
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
    public void insertAndGetAllTasks() throws Exception {
        mTaskDao.clearTable();
        Task task = new Task(1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task);
        Task task2 = new Task(2, "task 2", new Date().getTime());
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "task 3", new Date().getTime());
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.size(),2);
        assertEquals(allTasks.get(0).getName(),task.getName());
        assertNotEquals(allTasks.get(1).getName(),task3.getName());
        assertEquals(allTasks.get(1).getName(),task2.getName());
    }

    @Test
    public void deleteAll() throws Exception {
        mTaskDao.clearTable();
        Task task = new Task(1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task);
        Task task2 = new Task(2, "task 2", new Date().getTime());
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "task 3", new Date().getTime());
        mTaskDao.insertTask(task3);
        mTaskDao.clearTable();
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.size(),0);
    }

    @Test
    public void delete() throws Exception {
        mTaskDao.clearTable();
        Task task = new Task(1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task);
        Task task2 = new Task(2, "task 2", new Date().getTime());
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "task 3", new Date().getTime());
        mTaskDao.insertTask(task3);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.size(),3);
        mTaskDao.deleteTask(3);
        allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.size(),2);
    }

    @Test
    public void test_projects() throws Exception {
        mTaskDao.clearTable();
        Task task1 = new Task(1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task1);
        Task task2 = new Task(2, "task 2", new Date().getTime());
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "task 3", new Date().getTime());
        mTaskDao.insertTask(task3);

        assertEquals("Projet Tartampion", mProjectDao.getProject(task1.getProjectId()).getName());
        assertEquals("Projet Lucidia", mProjectDao.getProject(task2.getProjectId()).getName());
        assertEquals("Projet Circus", mProjectDao.getProject(task3.getProjectId()).getName());
    }

    @Test
    public void test_az_comparator() throws Exception {
        mTaskDao.clearTable();
        Task task1 = new Task(1, "aaa", 123);
        mTaskDao.insertTask(task1);
        Task task2 = new Task(2, "zzz", 124);
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "hhh", 125);
        mTaskDao.insertTask(task3);

        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        Collections.sort(allTasks, new Task.TaskAZComparator());

        assertEquals(allTasks.get(0).getName(), task1.getName());
        assertEquals(allTasks.get(1).getName(), task3.getName());
        assertEquals(allTasks.get(2).getName(), task2.getName());
    }

    @Test
    public void test_za_comparator() throws Exception {
        mTaskDao.clearTable();
        Task task1 = new Task(1, "aaa", 123);
        mTaskDao.insertTask(task1);
        Task task2 = new Task(2, "zzz", 124);
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "hhh", 125);
        mTaskDao.insertTask(task3);

        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        Collections.sort(allTasks, new Task.TaskZAComparator());

        assertEquals(allTasks.get(0).getName(), task2.getName());
        assertEquals(allTasks.get(1).getName(), task3.getName());
        assertEquals(allTasks.get(2).getName(), task1.getName());
    }

    @Test
    public void test_recent_comparator() throws Exception {
        mTaskDao.clearTable();
        Task task1 = new Task(1, "aaa", 123);
        mTaskDao.insertTask(task1);
        Task task2 = new Task(2, "zzz", 124);
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "hhh", 125);
        mTaskDao.insertTask(task3);

        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        Collections.sort(allTasks, new Task.TaskRecentComparator());

        assertEquals(allTasks.get(0).getName(), task3.getName());
        assertEquals(allTasks.get(1).getName(), task2.getName());
        assertEquals(allTasks.get(2).getName(), task1.getName());
    }

    @Test
    public void test_old_comparator() throws Exception {
        mTaskDao.clearTable();
        Task task1 = new Task(1, "aaa", 123);
        mTaskDao.insertTask(task1);
        Task task2 = new Task(2, "zzz", 124);
        mTaskDao.insertTask(task2);
        Task task3 = new Task(3, "hhh", 125);
        mTaskDao.insertTask(task3);

        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        Collections.sort(allTasks, new Task.TaskOldComparator());

        assertEquals(allTasks.get(0).getName(), task1.getName());
        assertEquals(allTasks.get(1).getName(), task2.getName());
        assertEquals(allTasks.get(2).getName(), task3.getName());
    }
}