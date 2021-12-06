package pl.pbrodziak.repository;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.Lesson;

import java.util.List;

/**
 * Spring Data SQL repository for the Lesson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

    @Query("Select lesson from Lesson lesson where lesson.course.user.login = ?#{principal.username}")
    List<Lesson> findAllMyLessonTeacher();

    @Query("Select lesson from Lesson lesson where lesson.course.id in " +
        "(select course.id from Course course " +
        "left join CourseUser courseUser on course.id = courseUser.course.id " +
        "left join User user on courseUser.user.id = user.id " +
        "where user." +
        " = " +
        "?#{principal.username})")
    List<Lesson> findAllMyLessonUser();

}
