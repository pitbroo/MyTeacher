package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.TimeShit;

/**
 * Service Interface for managing {@link TimeShit}.
 */
public interface TimeShitService {
    /**
     * Save a timeShit.
     *
     * @param timeShit the entity to save.
     * @return the persisted entity.
     */
    TimeShit save(TimeShit timeShit);

    /**
     * Partially updates a timeShit.
     *
     * @param timeShit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimeShit> partialUpdate(TimeShit timeShit);

    /**
     * Get all the timeShits.
     *
     * @return the list of entities.
     */
    List<TimeShit> findAll();

    /**
     * Get the "id" timeShit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimeShit> findOne(Long id);

    /**
     * Delete the "id" timeShit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
