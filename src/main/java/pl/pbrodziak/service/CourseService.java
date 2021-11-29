package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.Course;

/**
 * Service Interface for managing {@link Course}.
 */
public interface CourseService {
    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    Course save(Course course);

    /**
     * Partially updates a course.
     *
     * @param course the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Course> partialUpdate(Course course);

    /**
     * Get all the courses.
     *
     * @return the list of entities.
     */
    List<Course> findAll();
    /**
     * Get all the Course where Payment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Course> findAllWherePaymentIsNull();

    /**
     * Get the "id" course.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Course> findOne(Long id);

    /**
     * Delete the "id" course.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
