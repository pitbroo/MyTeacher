package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.Point;

/**
 * Service Interface for managing {@link Point}.
 */
public interface PointService {
    /**
     * Save a point.
     *
     * @param point the entity to save.
     * @return the persisted entity.
     */
    Point save(Point point);

    /**
     * Partially updates a point.
     *
     * @param point the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Point> partialUpdate(Point point);

    /**
     * Get all the points.
     *
     * @return the list of entities.
     */
    List<Point> findAll();

    /**
     * Get the "id" point.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Point> findOne(Long id);

    /**
     * Delete the "id" point.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
