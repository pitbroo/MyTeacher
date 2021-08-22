package pl.pbrodziak.education.entity.course;

import lombok.Data;
import pl.pbrodziak.education.entity.User;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int presenceId;
    @Column
    private int lessonID;
    @Column
    private LocalDateTime dateOfEntry;
    @Column
    private int userId;

    @ManyToOne
    private Lesson lesson;
    @OneToMany(mappedBy = "timeSheet")
    private List<User> user;

}
