package ru.alexander.projects.auths.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.alexander.projects.tasks.data.entities.TaskCommentEntity;
import ru.alexander.projects.tasks.data.entities.TaskEntity;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedEntityGraph(name = "user_with_comments", includeAllAttributes = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    UserRole role;

    @OneToMany(mappedBy = "owner")
    @Column(name = "comment_id", nullable = false)
    List<TaskCommentEntity> comments;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "created_task_id", nullable = false)
    List<TaskEntity> createdTasks;

    @OneToMany(mappedBy = "contractor")
    @Column(name = "task_to_complete_id", nullable = false)
    List<TaskEntity> tasksToComplete;
}
