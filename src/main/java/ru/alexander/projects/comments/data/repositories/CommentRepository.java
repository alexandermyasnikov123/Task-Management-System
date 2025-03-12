package ru.alexander.projects.comments.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexander.projects.comments.data.entities.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByTaskId(Long taskId, Pageable page);
}
