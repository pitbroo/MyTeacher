package pl.pbrodziak.education.service;


import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.dto.CourseDto;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();

    Course addCourse(CourseDto courseDto);
}
