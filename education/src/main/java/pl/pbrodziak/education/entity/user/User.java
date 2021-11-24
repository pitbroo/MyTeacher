package pl.pbrodziak.education.entity.user;

import lombok.Data;
import pl.pbrodziak.education.entity.EmailNotification;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.course.Lesson;
import pl.pbrodziak.education.entity.course.Task;
import pl.pbrodziak.education.entity.course.TimeSheet;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column
    private Date lastLoginDate;
    @Column
    private Date lastLoginDateDisplay;
    @Column
    private Date joinDate;

    //private String[] Autorities
    //private String[] Role
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
