package pl.pbrodziak.education.entity.course;

import lombok.Data;
import pl.pbrodziak.education.entity.EmailNotification;
import pl.pbrodziak.education.entity.user.User;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    @Column
    private String instructorId;
    @Column
    private String courseName;
    @Column
    private String price;
    @Column
    private String category;

    @ManyToMany(mappedBy = "courses")
    private List<User> usersList;
    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;
    @ManyToOne
    private EmailNotification emailNotification;

}
