package pl.pbrodziak.education.entity.course;

import lombok.Data;
import pl.pbrodziak.education.entity.EmailNotification;
import pl.pbrodziak.education.entity.User;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    @Column
    private String instructorID;
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
