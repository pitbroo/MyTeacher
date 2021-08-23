package pl.pbrodziak.education.service.impl;

import org.springframework.stereotype.Service;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.dto.CourseDto;
import pl.pbrodziak.education.entity.dto.mapper.CourseMapper;
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

    @Override
    public Course addCourse(CourseDto courseDto) {
        try{
            return courseRepository.save(CourseMapper.INSTANCE.courseDtoToCourse(courseDto));
        }catch (NullPointerException e){
            throw new NullPointerException("Wystąpił bład");
        }

    }
}
