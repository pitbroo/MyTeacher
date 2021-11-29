package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.TaskSolved;

/**
 * Service Interface for managing {@link TaskSolved}.
 */
public interface TaskSolvedService {
    /**
     * Save a taskSolved.
     *
     * @param taskSolved the entity to save.
     * @return the persisted entity.
     */
    TaskSolved save(TaskSolved taskSolved);

    /**
     * Partially updates a taskSolved.
     *
     * @param taskSolved the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskSolved> partialUpdate(TaskSolved taskSolved);

    /**
     * Get all the taskSolveds.
     *
     * @return the list of entities.
     */
    List<TaskSolved> findAll();

    /**
     * Get the "id" taskSolved.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskSolved> findOne(Long id);

    /**
     * Delete the "id" taskSolved.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
