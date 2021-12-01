package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.Course;
import pl.pbrodziak.repository.CourseRepository;
import pl.pbrodziak.service.CourseService;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> partialUpdate(Course course) {
        log.debug("Request to partially update Course : {}", course);

        return courseRepository
            .findById(course.getId())
            .map(
                existingCourse -> {
                    if (course.getName() != null) {
                        existingCourse.setName(course.getName());
                    }
                    if (course.getValue() != null) {
                        existingCourse.setValue(course.getValue());
                    }
                    if (course.getPrice() != null) {
                        existingCourse.setPrice(course.getPrice());
                    }
                    if (course.getCategory() != null) {
                        existingCourse.setCategory(course.getCategory());
                    }
                    if (course.getDescription() != null) {
                        existingCourse.setDescription(course.getDescription());
                    }

                    return existingCourse;
                }
            )
            .map(courseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        log.debug("Request to get all Courses");
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findByUserIsCurrentUser() {
        log.debug("Request to Find all by current user");
        return courseRepository.findByUserIsCurrentUser();
    }

    /**
     *  Get all the courses where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Course> findAllWherePaymentIsNull() {
        log.debug("Request to get all courses where Payment is null");
        return StreamSupport
            .stream(courseRepository.findAll().spliterator(), false)
            .filter(course -> course.getPayment() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> findAllUserCoursesByCurrentCourse() {
        return courseRepository.findAllUserCoursesByCurrentCourse();
    }
}
