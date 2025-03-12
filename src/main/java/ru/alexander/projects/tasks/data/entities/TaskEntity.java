package ru.alexander.projects.tasks.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.alexander.projects.auths.data.entities.UserEntity;
import ru.alexander.projects.comments.data.entities.CommentEntity;

import java.util.List;

@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "tasks")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedEntityGraph(name = "task_with_all_attributes", includeAllAttributes = true)
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

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "comment_id", nullable = false)
    List<CommentEntity> comments;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_task_owner"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    UserEntity owner;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", foreignKey = @ForeignKey(name = "fk_task_contractor"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    UserEntity contractor;
}
