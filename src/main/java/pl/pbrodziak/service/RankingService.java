package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.Ranking;

/**
 * Service Interface for managing {@link Ranking}.
 */
public interface RankingService {
    /**
     * Save a ranking.
     *
     * @param ranking the entity to save.
     * @return the persisted entity.
     */
    Ranking save(Ranking ranking);

    /**
     * Partially updates a ranking.
     *
     * @param ranking the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ranking> partialUpdate(Ranking ranking);

    /**
     * Get all the rankings.
     *
     * @return the list of entities.
     */
    List<Ranking> findAll();

    /**
     * Get the "id" ranking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ranking> findOne(Long id);

    /**
     * Delete the "id" ranking.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
