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
import pl.pbrodziak.domain.Lesson;
import pl.pbrodziak.repository.LessonRepository;
import pl.pbrodziak.service.criteria.LessonCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Lesson} entities in the database.
 * The main input is a {@link LessonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Lesson} or a {@link Page} of {@link Lesson} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonQueryService extends QueryService<Lesson> {

    private final Logger log = LoggerFactory.getLogger(LessonQueryService.class);

    private final LessonRepository lessonRepository;

    public LessonQueryService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    /**
     * Return a {@link List} of {@link Lesson} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Lesson> findByCriteria(LessonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lesson> specification = createSpecification(criteria);
        return lessonRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Lesson} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Lesson> findByCriteria(LessonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lesson> specification = createSpecification(criteria);
        return lessonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LessonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lesson> specification = createSpecification(criteria);
        return lessonRepository.count(specification);
    }

    /**
     * Function to convert {@link LessonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lesson> createSpecification(LessonCriteria criteria) {
        Specification<Lesson> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lesson_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Lesson_.status));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), Lesson_.dateStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), Lesson_.dateEnd));
            }
            if (criteria.getClassroomOrAddres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassroomOrAddres(), Lesson_.classroomOrAddres));
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCourseId(), root -> root.join(Lesson_.course, JoinType.LEFT).get(Course_.id))
                    );
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaskId(), root -> root.join(Lesson_.tasks, JoinType.LEFT).get(Task_.id))
                    );
            }
        }
        return specification;
    }
}
