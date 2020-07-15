package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.db.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

public class ProjectRepository {

    private final ProjectDao mProjectDao;

    public ProjectRepository(ProjectDao projectDao) {
        this.mProjectDao = projectDao;
    }

    // Get project
    public LiveData<Project> getProject(long projectId) {
        return mProjectDao.getProject(projectId);
    }

    // Get all projects
    public LiveData<List<Project>> getAllProject() {
        return mProjectDao.getAllProject();
    }
}
