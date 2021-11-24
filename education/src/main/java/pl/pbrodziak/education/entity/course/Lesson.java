package pl.pbrodziak.education.entity.course;

import lombok.Data;
import pl.pbrodziak.education.entity.EmailNotification;
import pl.pbrodziak.education.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int lessonId;
    @Column
    int instructorId;
    @Column
    int status;
    @Column
    LocalDateTime dataTimeStart;
    @Column
    LocalDateTime dataTimeFinish;
    @Column
    String lessonTitle;
    @Column
    String lessonDescription;
    @Column
    String lessonAddres;

    @OneToMany(mappedBy = "lesson")
    private List<TimeSheet> timeSheets;
    @ManyToOne
    private Course course;
    @ManyToMany(mappedBy = "lessons")
    private List<User> users;
    @OneToMany(mappedBy = "lesson")
    private List<Task> Task;
    @ManyToOne
    private EmailNotification emailNotification;


}
