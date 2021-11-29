package pl.pbrodziak.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.*; // for static metamodels
import pl.pbrodziak.domain.TimeShit;
import pl.pbrodziak.repository.TimeShitRepository;
import pl.pbrodziak.service.criteria.TimeShitCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TimeShit} entities in the database.
 * The main input is a {@link TimeShitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TimeShit} or a {@link Page} of {@link TimeShit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimeShitQueryService extends QueryService<TimeShit> {

    private final Logger log = LoggerFactory.getLogger(TimeShitQueryService.class);

    private final TimeShitRepository timeShitRepository;

    public TimeShitQueryService(TimeShitRepository timeShitRepository) {
        this.timeShitRepository = timeShitRepository;
    }

    /**
     * Return a {@link List} of {@link TimeShit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TimeShit> findByCriteria(TimeShitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TimeShit> specification = createSpecification(criteria);
        return timeShitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TimeShit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TimeShit> findByCriteria(TimeShitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TimeShit> specification = createSpecification(criteria);
        return timeShitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimeShitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TimeShit> specification = createSpecification(criteria);
        return timeShitRepository.count(specification);
    }

    /**
     * Function to convert {@link TimeShitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TimeShit> createSpecification(TimeShitCriteria criteria) {
        Specification<TimeShit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TimeShit_.id));
            }
            if (criteria.getPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getPresent(), TimeShit_.present));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), TimeShit_.date));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(TimeShit_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
