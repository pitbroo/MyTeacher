package pl.pbrodziak.education.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.pbrodziak.education.entity.course.Course;
import pl.pbrodziak.education.entity.dto.CourseDto;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "instructorId", target = "instructorId")
    @Mapping(source = "courseName", target = "courseName")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "category", target = "category")
    CourseDto courseToCourseDto(Course course);

    @Mapping(source = "instructorId", target = "instructorId")
    @Mapping(source = "courseName", target = "courseName")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "category", target = "category")
    Course courseDtoToCourse(CourseDto courseDto);
}
