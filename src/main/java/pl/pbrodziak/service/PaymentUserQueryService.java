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
import pl.pbrodziak.domain.PaymentUser;
import pl.pbrodziak.repository.PaymentUserRepository;
import pl.pbrodziak.service.criteria.PaymentUserCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PaymentUser} entities in the database.
 * The main input is a {@link PaymentUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentUser} or a {@link Page} of {@link PaymentUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentUserQueryService extends QueryService<PaymentUser> {

    private final Logger log = LoggerFactory.getLogger(PaymentUserQueryService.class);

    private final PaymentUserRepository paymentUserRepository;

    public PaymentUserQueryService(PaymentUserRepository paymentUserRepository) {
        this.paymentUserRepository = paymentUserRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentUser> findByCriteria(PaymentUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentUser> specification = createSpecification(criteria);
        return paymentUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PaymentUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentUser> findByCriteria(PaymentUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentUser> specification = createSpecification(criteria);
        return paymentUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentUser> specification = createSpecification(criteria);
        return paymentUserRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentUser> createSpecification(PaymentUserCriteria criteria) {
        Specification<PaymentUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentUser_.id));
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPaymentId(), root -> root.join(PaymentUser_.payment, JoinType.LEFT).get(Payment_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(PaymentUser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
