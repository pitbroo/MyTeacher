package pl.pbrodziak.education.controller;

import org.springframework.web.bind.annotation.*;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.dto.CourseDto;
import pl.pbrodziak.education.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("course")
@CrossOrigin("http://localhost:3000")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("all")
    public List<Course> getCourse(){
        return courseService.getAllCourses();
    }

    @PostMapping("add")
    public Course addCourse(@RequestBody CourseDto courseDto){
        return courseService.addCourse(courseDto);
    }
}
