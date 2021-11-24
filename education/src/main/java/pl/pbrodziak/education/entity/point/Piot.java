package pl.pbrodziak.education.entity.point;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Piot {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        int pointId;
        @Column
        int userId;
        @Column
        int courseId;
        @Column
        int value;
}
