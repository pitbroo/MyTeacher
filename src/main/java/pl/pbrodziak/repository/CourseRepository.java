package pl.pbrodziak.repository;

import java.util.List;

import org.hibernate.sql.Select;
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


    @Query("SELECT course from Course course  " +
        "left JOIN CourseUser courseUser on course.id = courseUser.course.id " +
        "left JOIN User user on courseUser.user.id = user.id " +
        "WHERE " +
            "user.id IS NULL " +
            "OR course.id not in "
           + "(SELECT course.id from Course course JOIN " +
            "CourseUser courseUser on course.id = courseUser.course.id JOIN " +
            "User user on courseUser.user.id = user.id where user.login = ?#{principal.username})")
    List<Course> findByUserIsNotCurrentUser();


}
