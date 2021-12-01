package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.Course;

import static javax.persistence.criteria.JoinType.INNER;

/**
 * Spring Data SQL repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    @Query("select course from Course course where course.user.login = ?#{principal.username}")
    List<Course> findByUserIsCurrentUser();

    @Query("SELECT course from Course course INNER JOIN CourseUser courseUser on course.id = courseUser.course.id INNER JOIN User user on courseUser.user.id = user.id where user.login = ?#{principal.username}")
    List<Course> findAllUserCoursesByCurrentCourse();
//    @Query("SELECT jhi_user.login, course.name from course INNER JOIN course_user on course.id = course_user.course_id INNER JOIN jhi_user on course_user.user_id = jhi_user.id where jhi_user.login = ?#{principal.username}")
//    List<Course> findAllUserCoursesByCurrentCourse();
}
