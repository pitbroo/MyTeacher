package pl.pbrodziak.education.service.impl;

import org.springframework.stereotype.Service;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.repository.CourseRepository;
import pl.pbrodziak.education.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    public final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses(){

        return courseRepository.findAll();
    }
}
