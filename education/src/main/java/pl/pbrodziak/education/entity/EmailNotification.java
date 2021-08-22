package pl.pbrodziak.education.entity;

import lombok.Data;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.course.Lesson;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int notificationId;
    @Column
    int lessonID;
    @Column
    int userId;
    @Column
    LocalDateTime notifiactionDataTime;
    @Column
    String title;
    @Column
    String content;

    @OneToMany(mappedBy = "emailNotification")
    private List<Lesson> lessons;
    @OneToMany(mappedBy = "emailNotification")
    private List<User> users;
    @OneToMany(mappedBy = "emailNotification")
    private List<Course> courses;
    //GIT INIT~!!!!!!!!!!!!!!!!!!!!!!!!


}
