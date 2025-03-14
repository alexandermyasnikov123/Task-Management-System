package ru.alexander.projects.tasks.data.repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexander.projects.tasks.data.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @NonNull
    @Query(value = """
            from TaskEntity t
            where t.title ilike coalesce(:query, t.title)
            or t.details ilike coalesce(:query, t.details)""")
    Page<TaskEntity> findFilteredTasks(String query, @NonNull Pageable pageable);
}
