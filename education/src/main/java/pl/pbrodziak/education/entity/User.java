package pl.pbrodziak.education.entity;

import lombok.Data;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.course.Lesson;
import pl.pbrodziak.education.entity.course.Task;
import pl.pbrodziak.education.entity.course.TimeSheet;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column
    private String name;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String userRole;
    @Column
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Point> points;
    @ManyToOne
    private EmailNotification emailNotification;
    @ManyToMany()
    private List<Lesson> lessons;
    @ManyToMany()
    private List<Task> tasks;
    @ManyToMany()
    private List<Course> courses;
    @ManyToOne
    private TimeSheet timeSheet;


}
