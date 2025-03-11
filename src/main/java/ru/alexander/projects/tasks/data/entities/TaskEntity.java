package ru.alexander.projects.tasks.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.alexander.projects.auths.data.entities.UserEntity;

import java.util.List;

@Data
@Table(name = "tasks")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String details;

    /// Of course, it's better to use different table instead of hardcoded enums
    /// But it will slow down development of this backend service
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    TaskStatus status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    TaskPriority priority;

    @Column(nullable = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<TaskCommentEntity> comments;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    UserEntity contractor;
}
