package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.CourseUser;
import pl.pbrodziak.domain.User;

/**
 * Service Interface for managing {@link CourseUser}.
 */
public interface CourseUserService {
    /**
     * Save a courseUser.
     *
     * @param courseUser the entity to save.
     * @return the persisted entity.
     */
    CourseUser save(CourseUser courseUser);

    /**
     * Partially updates a courseUser.
     *
     * @param courseUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseUser> partialUpdate(CourseUser courseUser);

    /**
     * Get all the courseUsers.
     *
     * @return the list of entities.
     */
    List<CourseUser> findAll();

    /**
     * Get the "id" courseUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseUser> findOne(Long id);

    /**
     * Delete the "id" courseUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CourseUser> findAllDependUser();
}
