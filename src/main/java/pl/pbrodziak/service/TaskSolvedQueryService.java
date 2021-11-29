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
import pl.pbrodziak.domain.TaskSolved;
import pl.pbrodziak.repository.TaskSolvedRepository;
import pl.pbrodziak.service.criteria.TaskSolvedCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TaskSolved} entities in the database.
 * The main input is a {@link TaskSolvedCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskSolved} or a {@link Page} of {@link TaskSolved} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskSolvedQueryService extends QueryService<TaskSolved> {

    private final Logger log = LoggerFactory.getLogger(TaskSolvedQueryService.class);

    private final TaskSolvedRepository taskSolvedRepository;

    public TaskSolvedQueryService(TaskSolvedRepository taskSolvedRepository) {
        this.taskSolvedRepository = taskSolvedRepository;
    }

    /**
     * Return a {@link List} of {@link TaskSolved} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskSolved> findByCriteria(TaskSolvedCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaskSolved> specification = createSpecification(criteria);
        return taskSolvedRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TaskSolved} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskSolved> findByCriteria(TaskSolvedCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskSolved> specification = createSpecification(criteria);
        return taskSolvedRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskSolvedCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaskSolved> specification = createSpecification(criteria);
        return taskSolvedRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskSolvedCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskSolved> createSpecification(TaskSolvedCriteria criteria) {
        Specification<TaskSolved> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaskSolved_.id));
            }
            if (criteria.getPointGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointGrade(), TaskSolved_.pointGrade));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), TaskSolved_.content));
            }
            if (criteria.getDeadline() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeadline(), TaskSolved_.deadline));
            }
            if (criteria.getSendDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSendDay(), TaskSolved_.sendDay));
            }
            if (criteria.getAnswer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnswer(), TaskSolved_.answer));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(TaskSolved_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaskId(), root -> root.join(TaskSolved_.task, JoinType.LEFT).get(Task_.id))
                    );
            }
        }
        return specification;
    }
}
