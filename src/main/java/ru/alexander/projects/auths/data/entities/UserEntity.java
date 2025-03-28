package ru.alexander.projects.auths.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.alexander.projects.comments.data.entities.CommentEntity;
import ru.alexander.projects.tasks.data.entities.TaskEntity;

import java.util.List;
import java.util.UUID;

@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    List<CommentEntity> comments;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TaskEntity> createdTasks;

    @OneToMany(mappedBy = "contractor")
    List<TaskEntity> tasksToComplete;
}
