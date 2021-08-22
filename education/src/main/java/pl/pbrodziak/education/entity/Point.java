package pl.pbrodziak.education.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Point {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int pointId;
    @Column
    int userId;
    @Column
    int taskId;
    @Column
    int courseId;
    @Column
    LocalDateTime addingDate;
    @Column
    int value;

    @ManyToOne
    private User user;
}
