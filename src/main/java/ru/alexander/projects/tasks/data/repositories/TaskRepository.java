package ru.alexander.projects.tasks.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.alexander.projects.tasks.data.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @NonNull
    @Query(value = """
            from TaskEntity t
            join fetch t.comments com
            join fetch t.owner own
            join fetch t.contractor contr
            where t.title ilike coalesce(:query, t.title)
            or t.details ilike coalesce(:query, t.details)""")
    Page<TaskEntity> findFilteredTasks(@NonNull String query, @NonNull Pageable pageable);

    @NonNull
    @Override
    @EntityGraph(value = "task_with_all_attributes")
    Page<TaskEntity> findAll(@NonNull Pageable pageable);
}
