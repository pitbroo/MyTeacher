package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.Payment;

/**
 * Service Interface for managing {@link Payment}.
 */
public interface PaymentService {
    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    Payment save(Payment payment);

    /**
     * Partially updates a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Payment> partialUpdate(Payment payment);

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    List<Payment> findAll();

    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Payment> findOne(Long id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
