package com.em.taskmanager.entities.task;

import com.em.taskmanager.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.OffsetDateTime;

@Entity
@Table(name = "comments")
@Data
@Accessors(chain = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @UpdateTimestamp
    private OffsetDateTime updated;

    @CreationTimestamp
    private OffsetDateTime created;

}
