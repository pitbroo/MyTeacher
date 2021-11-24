package pl.pbrodziak.education.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.education.entity.course.Course;

import java.util.List;

@RepositoryRestResource
public interface CourseRepository extends JpaRepository<Course,Integer> {

    @RestResource(path = "category")
    List<Course> getCourseByCategory(@Param("category") String category);
}
