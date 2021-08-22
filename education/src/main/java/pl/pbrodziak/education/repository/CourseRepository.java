package pl.pbrodziak.education.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.education.entity.course.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
}
