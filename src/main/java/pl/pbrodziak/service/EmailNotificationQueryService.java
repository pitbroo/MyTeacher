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
import pl.pbrodziak.domain.EmailNotification;
import pl.pbrodziak.repository.EmailNotificationRepository;
import pl.pbrodziak.service.criteria.EmailNotificationCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EmailNotification} entities in the database.
 * The main input is a {@link EmailNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmailNotification} or a {@link Page} of {@link EmailNotification} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailNotificationQueryService extends QueryService<EmailNotification> {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationQueryService.class);

    private final EmailNotificationRepository emailNotificationRepository;

    public EmailNotificationQueryService(EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }

    /**
     * Return a {@link List} of {@link EmailNotification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmailNotification> findByCriteria(EmailNotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmailNotification> specification = createSpecification(criteria);
        return emailNotificationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmailNotification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailNotification> findByCriteria(EmailNotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailNotification> specification = createSpecification(criteria);
        return emailNotificationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailNotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmailNotification> specification = createSpecification(criteria);
        return emailNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailNotification> createSpecification(EmailNotificationCriteria criteria) {
        Specification<EmailNotification> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmailNotification_.id));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), EmailNotification_.content));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), EmailNotification_.time));
            }
            if (criteria.getTeacher() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeacher(), EmailNotification_.teacher));
            }
            if (criteria.getEmailNotificationUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmailNotificationUserId(),
                            root -> root.join(EmailNotification_.emailNotificationUsers, JoinType.LEFT).get(EmailNotificationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
