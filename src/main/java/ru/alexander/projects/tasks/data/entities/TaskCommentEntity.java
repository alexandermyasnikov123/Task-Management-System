package ru.alexander.projects.tasks.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import ru.alexander.projects.auths.data.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "comments")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    Long id;

    @Column(nullable = false)
    String comment;

    @Column(nullable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_comment_owner"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_task_comment"))
    TaskEntity task;
}
