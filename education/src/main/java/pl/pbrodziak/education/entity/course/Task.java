package pl.pbrodziak.education.entity.course;

import lombok.Data;
import pl.pbrodziak.education.entity.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int taskId;
    @Column
    int userId;
    @Column
    int courseId;
    @Column
    int lessonId;
    @Column
    String title;
    @Column
    String content;

    @ManyToMany(mappedBy = "tasks")
    private List<User> users;
    @ManyToOne
    private Lesson lesson;

}
