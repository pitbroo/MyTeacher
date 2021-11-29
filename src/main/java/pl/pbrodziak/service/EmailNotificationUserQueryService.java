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
import pl.pbrodziak.domain.EmailNotificationUser;
import pl.pbrodziak.repository.EmailNotificationUserRepository;
import pl.pbrodziak.service.criteria.EmailNotificationUserCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EmailNotificationUser} entities in the database.
 * The main input is a {@link EmailNotificationUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmailNotificationUser} or a {@link Page} of {@link EmailNotificationUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailNotificationUserQueryService extends QueryService<EmailNotificationUser> {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationUserQueryService.class);

    private final EmailNotificationUserRepository emailNotificationUserRepository;

    public EmailNotificationUserQueryService(EmailNotificationUserRepository emailNotificationUserRepository) {
        this.emailNotificationUserRepository = emailNotificationUserRepository;
    }

    /**
     * Return a {@link List} of {@link EmailNotificationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmailNotificationUser> findByCriteria(EmailNotificationUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmailNotificationUser> specification = createSpecification(criteria);
        return emailNotificationUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmailNotificationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailNotificationUser> findByCriteria(EmailNotificationUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailNotificationUser> specification = createSpecification(criteria);
        return emailNotificationUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailNotificationUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmailNotificationUser> specification = createSpecification(criteria);
        return emailNotificationUserRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailNotificationUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailNotificationUser> createSpecification(EmailNotificationUserCriteria criteria) {
        Specification<EmailNotificationUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmailNotificationUser_.id));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(EmailNotificationUser_.user, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getEmailNotificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmailNotificationId(),
                            root -> root.join(EmailNotificationUser_.emailNotification, JoinType.LEFT).get(EmailNotification_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
