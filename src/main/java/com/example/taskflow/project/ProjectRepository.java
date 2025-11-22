package com.example.taskflow.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByIdAndStatus(Long id, ProjectStatus status);
}
