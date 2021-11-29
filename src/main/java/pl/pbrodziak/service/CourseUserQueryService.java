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
import pl.pbrodziak.domain.CourseUser;
import pl.pbrodziak.repository.CourseUserRepository;
import pl.pbrodziak.service.criteria.CourseUserCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CourseUser} entities in the database.
 * The main input is a {@link CourseUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CourseUser} or a {@link Page} of {@link CourseUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseUserQueryService extends QueryService<CourseUser> {

    private final Logger log = LoggerFactory.getLogger(CourseUserQueryService.class);

    private final CourseUserRepository courseUserRepository;

    public CourseUserQueryService(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    /**
     * Return a {@link List} of {@link CourseUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CourseUser> findByCriteria(CourseUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CourseUser> specification = createSpecification(criteria);
        return courseUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CourseUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CourseUser> findByCriteria(CourseUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CourseUser> specification = createSpecification(criteria);
        return courseUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CourseUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CourseUser> specification = createSpecification(criteria);
        return courseUserRepository.count(specification);
    }

    /**
     * Function to convert {@link CourseUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CourseUser> createSpecification(CourseUserCriteria criteria) {
        Specification<CourseUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CourseUser_.id));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(CourseUser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCourseId(), root -> root.join(CourseUser_.course, JoinType.LEFT).get(Course_.id))
                    );
            }
        }
        return specification;
    }
}
